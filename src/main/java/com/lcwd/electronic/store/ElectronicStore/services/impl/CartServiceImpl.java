package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.general.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.exceptions.cartexceptions.MoreQuantityThanStockException;
import com.lcwd.electronic.store.ElectronicStore.exceptions.cartexceptions.ZeroQuantityException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.payload.cartpayload.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartItemRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {
    private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        // Fetch the user with userId
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User not found with given User ID"));
        // Take out the information of product quantity and product ID from AddItemToCartRequest request
        Integer quantity = request.getQuantity();
        if(quantity == 0) {
            throw new ZeroQuantityException("Quantity cannot be zero !!");
        }

        String productId = request.getProductId();

        // Fetch the product with productId
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourseNotFoundException(("User with given Id not found!!")));

        // Fix: Say if the quantity of the products requested is more than the stock quantity then
        //      it should not be added...
//        if(!product.isProductOutOfStock() || !product.isProductLive() || quantity > product.getProductStockQuantity()) {
//            throw new MoreQuantityThanStockException("Product " + product.getProductName() + "is not available in requested quantity!!");
//        }
        if(quantity > product.getProductStockQuantity()) {
            throw new MoreQuantityThanStockException("Product " + product.getProductName() + "is not available in requested quantity!!");
        }

        // Now using the user we will fetch the cart
        Optional<Cart> cart = cartRepository.findByUser(user);
        if(cart.isEmpty()) {
            // We don't find any cart associated with the given user.
            // We need to create a new Cart and assign all the things to that cart
            // 1. Create a new Cart
            Cart newCart = new Cart();
            // Set an Id for this newly created cart
            String uniqueCartId = UUID.randomUUID().toString();
            newCart.setCartId(uniqueCartId);
            // Attach the user with the new cart
            newCart.setUser(user);
            // 2. Now our job is to set the cart items
//            List<CartItem> cartItems = newCart.getCartItems(); // this will be an empty list
            List<CartItem> cartItems = new ArrayList<>(); // this will be an empty list

            CartItem newCartItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .totalPrice(product.getProductPrice() * quantity)
                    .cart(newCart)
                    .build();
            cartItems.add(newCartItem);
            // attached the list of cart Items to the newly created Cart
            newCart.setCartItems(cartItems);
            newCart.setTotalCartItems(cartItems.size());
            newCart.setTotalCartPrice(
                    cartItems.stream()
                            .mapToDouble(CartItem::getTotalPrice)
                            .sum()
            );
            Cart savedCart = cartRepository.save(newCart);
            return mapper.map(savedCart, CartDto.class);

        } else {
            // Cart for a user is already present so just make a new Cart item and assign to it...
            // But before that check if the user is adding the same product again or not..
            boolean isProductAlreadyPresentInCart = false;

            Cart existingCart = cart.get();
            List<CartItem> cartItems = existingCart.getCartItems();  // This will have previous cart items too.
            for(CartItem cartItem: cartItems) {
                //  If we find any product already in the cart just update the quantity and total price
                if(cartItem.getProduct().getProductId().equalsIgnoreCase(productId)) {
                    isProductAlreadyPresentInCart = true;
                    cartItem.setQuantity(cartItem.getQuantity()+quantity);
                    cartItem.setTotalPrice(cartItem.getQuantity()*product.getProductPrice());
                    existingCart.setTotalCartPrice(calculateTotalPrice(existingCart));
                }
            }
            if(!isProductAlreadyPresentInCart) {
                // This part executes when the product is not there in the cart
                // So create a new cart item.
                CartItem newCartItem = CartItem.builder()
                        .product(product)
                        .quantity(quantity)
                        .totalPrice(product.getProductPrice() * quantity)
                        .cart(existingCart)
                        .build();
                cartItems.add(newCartItem);
                // attached the list of cart Items to the newly created Cart
                existingCart.setCartItems(cartItems);
                existingCart.setTotalCartItems(cartItems.size());
                existingCart.setTotalCartPrice(calculateTotalPrice(existingCart));
            }
            Cart savedCart = cartRepository.save(existingCart);
            return Helper.mapCartEntityToCartDto(savedCart);
        }
    }

    @Override
    public void removeItemFromCart(String userId, Long cartItemId) {
        CartItem cartItemToBeRemoved = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourseNotFoundException("Cart Item with given id not found !!"));
        int quantityToBeReduced = cartItemToBeRemoved.getQuantity();
        Double priceToBeReduced = cartItemToBeRemoved.getTotalPrice();
        logger.info("Price that is going to be reduced : {}", priceToBeReduced);
        cartItemRepository.delete(cartItemToBeRemoved);

        // Update the cart total price and cart item quantity
        Cart updatedCart = cartRepository.findByUser_UserId(userId).orElseThrow(() -> new ResourseNotFoundException(("Cart for given user not found !!")));
        updatedCart.setTotalCartItems(updatedCart.getTotalCartItems()-1);
        updatedCart.setTotalCartPrice(calculateTotalPrice(updatedCart));

        cartRepository.save(updatedCart);
    }


    // TODO: 12/23/2024  
    @Override
    public void removeAllFromCart(String userId) {
        // Fetch the user for DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User with given Id not found!!"));

        // Fetch the Cart that is associated with the user
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourseNotFoundException("Cart associated to the given user not found !!"));
        cart.getCartItems().clear();
        cart.setTotalCartItems(0);
        cart.setTotalCartPrice(0.0);
        cartRepository.save(cart);

    }

    @Override
    public CartDto searchCartByUser(String userId) {
        Cart cart = cartRepository.findByUser_UserId(userId).orElseThrow(() -> new ResourseNotFoundException("Cart not fund with given UserId"));
        return Helper.mapCartEntityToCartDto(cart);
    }

    // Helper functions

    // Calculates the Sum of Total price
    private Double calculateTotalPrice(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        double sum = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        return sum;
    }
}
