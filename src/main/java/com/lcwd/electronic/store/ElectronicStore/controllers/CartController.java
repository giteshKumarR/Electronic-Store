package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.payload.cartpayload.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart-api")
public class CartController {
    @Autowired
    private CartService cartService;
    // Add items to cart
    @PostMapping("/add-item/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable String userId) {
        return new ResponseEntity<>(cartService.addItemToCart(userId, request), HttpStatus.OK);
    }

    // Remove item from cart
    @PutMapping("/remove-item/{userId}/item/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeCartItem(@PathVariable String userId, @PathVariable Long cartItemId) {
        cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Cart Item removed Sucessfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // Remove all items from cart
    @PutMapping("/remove-all-items/{userId}")
    public ResponseEntity<ApiResponseMessage> removeAllCartItems(@PathVariable String userId) {
        cartService.removeAllFromCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("All Cart items removed successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // Search Cart by User (We have implemented this functionality in User Module)
    @GetMapping("/searchCartByUser/{userId}")
    public ResponseEntity<CartDto> searchUserCart(@PathVariable String userId) {
        return new ResponseEntity<>(cartService.searchCartByUser(userId), HttpStatus.FOUND);
    }
}
