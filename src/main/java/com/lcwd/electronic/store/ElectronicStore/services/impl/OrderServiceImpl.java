package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.entities.*;
import com.lcwd.electronic.store.ElectronicStore.enums.OrderStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentMethod;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.ShippingMethod;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.exceptions.cartexceptions.EmptyCartException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.OrderRequest;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.UpdateOrderRequestAdmin;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.UpdateOrderRequestUser;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.OrderRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
@Service
public class OrderServiceImpl implements OrderService {
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ModelMapper mapper;
    @Override
    public OrderDto createOrderFromCart(String userId, OrderRequest orderRequest) {
        // OrderRequest Object will contain all the details that about the addresses and payment from the user
        // with userId

        // 1. Check if the user is present or not
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException(("User with given Id not found !!")));

        // 2. Check if the cart is present or not for the user.
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourseNotFoundException("Cart for the given user not found!!"));

        // 3. Validate the cart Items
        validateCartItems(cart);

        // 4. Now Build the Order Object
        Order order = new Order();
        // Assign the order a unique orderId
        String uniqueId = UUID.randomUUID().toString();
        order.setOrderId(uniqueId);
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        // Converting cart items to order items
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> convertToOrderItem(cartItem, order))
                .toList();
        order.setOrderItems(orderItems);

        // Subtotal here means the cost before TAX is applied
        Double subtotal = calculateSubtotal(orderItems);
        order.setTotalBeforeTax(subtotal);

        // Tax --> GST (18%)
        Double tax = subtotal*0.18;
        order.setTax(tax);

        // Shipping Cost is 5%
        Double shipCost = subtotal*0.05;
        order.setShippingCost(shipCost);

        order.setTotalAmountAfterTax(subtotal + tax + shipCost);

        order.setOrderDate(LocalDateTime.now());

        order.setStatus(OrderStatus.PENDING); // Initially the order will be in PENDING status

        // Following information will be pulled from the orderRequest Object

        // Set Addresses
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setBillingAddress(orderRequest.getBillingAddress());

        // Set the Payment info
        order.setPaymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()));

        order.setPaymentStatus(PaymentStatus.NOT_PAID); // initially it is not paid

        // Set the Order notes
        order.setOrderNotes(orderRequest.getOrderNotes());

        // Set the shipping mode details
        order.setShippingMethod(orderRequest.getShippingMethod());

        // set the estimated delivery date
        order.setEstimatedDeliveryDate(
                calculateEstimatedDeliveryDate(orderRequest.getShippingMethod())
        );

        // Now clear all the items from the cart
        cartService.removeAllFromCart(userId);

        // save the order
        Order savedOrder = orderRepository.save(order);
        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public PagableResponse<OrderDto> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> pageObject = orderRepository.findAll(pagable);
        PagableResponse<OrderDto> pagableResponse = Helper.getPagableResponse(pageObject, OrderDto.class);

        return pagableResponse;
    }

    @Override
    public List<OrderDto> getAllOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User with given ID not found !!"));
        List<Order> userOrders = orderRepository.findByUser(user);
        return userOrders.stream()
                .map(order -> mapper.map(order, OrderDto.class))
                .toList();
    }

    @Override
    public void deleteOrder(String userId, String orderId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User with given Id not found !!"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourseNotFoundException("Order with given Id not found !!"));
        orderRepository.delete(order);
    }

    @Override
    public OrderDto updateOrderUser(String userId, UpdateOrderRequestUser updateOrderRequestUser) {
        return null;
    }

    @Override
    public OrderDto searchByOrderNumber(String orderNumber) {
        return mapper.map(orderRepository.findByOrderNumber(orderNumber), OrderDto.class);
    }

    @Override
    public OrderDto updateOrderAdmin(String userId, UpdateOrderRequestAdmin updateOrderRequestAdmin) {
        return null;
    }

    // Helper Functions
    private LocalDateTime calculateEstimatedDeliveryDate(ShippingMethod shippingMethod) {
        // Implementation for calculating estimated delivery date based on shipping method
        return LocalDateTime.now().plusDays(
                switch(shippingMethod.toString().toUpperCase()) {
                    case "EXPRESS" -> 3;
                    case "PRIME" -> 2;
                    default -> 5;
                }
        );
    }

    private Double calculateSubtotal(List<OrderItem> orderItems) {
        // Calculates sum of all the order items
        return  orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    private OrderItem convertToOrderItem(CartItem cartItem, Order order) {
        // converts the cart items to Order item
        OrderItem build = OrderItem.builder()
                .quantity(Long.valueOf(cartItem.getQuantity()))
                .totalPrice(cartItem.getTotalPrice())
                .product(cartItem.getProduct())
                .order(order)
                .build();
        return build;
    }

    private void validateCartItems(Cart cart) {
        if(cart.getCartItems().isEmpty()) {
            throw new EmptyCartException("Cart is Empty !!");
        }
        // Other logic will be added if necessary
    }
    private String generateOrderNumber() {
        // Implementation to generate unique order number
        return "ORD-" + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) +
                "-" + UUID.randomUUID().toString().substring(0,5).toUpperCase();
    }
}
