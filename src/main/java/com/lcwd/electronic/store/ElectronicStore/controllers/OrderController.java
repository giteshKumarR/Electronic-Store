package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.OrderRequest;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.UpdateOrderRequestAdmin;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.UpdateOrderRequestUser;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/order-api")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;

    // Create Order
    @PostMapping("/create-order/{userId}")
    public ResponseEntity<OrderDto> createOrder(
            @PathVariable String userId,
            @Valid @RequestBody OrderRequest orderRequest
            ) {
        return new ResponseEntity<>(orderService.createOrderFromCart(userId,orderRequest), HttpStatus.CREATED);
    }

    // Get All the Orders
    @GetMapping("/get-all-orders")
    public ResponseEntity<PagableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderNumber", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return new ResponseEntity<>(orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    // Get all orders of a User (We have implemented this functionality in User Module)
    @GetMapping("/get-user-orders/{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable String userId) {
        return new ResponseEntity<>(orderService.getAllOrdersOfUser(userId), HttpStatus.OK);
    }

    // Delete order
    @DeleteMapping("/delete-order/{userId}/order/{orderId}")
    public ResponseEntity<ApiResponseMessage> deleteOrder(@PathVariable String userId,
                                                          @PathVariable String orderId) {
        orderService.deleteOrder(userId, orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Order deleted successfully..")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/get-by-order-number/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return new ResponseEntity<>(orderService.searchByOrderNumber(orderNumber), HttpStatus.OK);
    }

    // Update API's
    // Normal User
    @PutMapping("/update-order/{userId}/user")
    public ResponseEntity<OrderDto> updateOrderUser(
            @PathVariable String userId,
            @Valid @RequestBody UpdateOrderRequestUser updateOrderRequestUser
            ) {
        return new ResponseEntity<>(orderService.updateOrderUser(userId, updateOrderRequestUser), HttpStatus.OK);
    }

    // Admin
    @PutMapping("/update-order/{userId}/admin")
    public ResponseEntity<OrderDto> updateOrderAdmin(
            @PathVariable String userId,
            @Valid @RequestBody UpdateOrderRequestAdmin updateOrderRequestAdmin
            ) {
        return new ResponseEntity<>(orderService.updateOrderAdmin(userId, updateOrderRequestAdmin), HttpStatus.OK);
    }
}
