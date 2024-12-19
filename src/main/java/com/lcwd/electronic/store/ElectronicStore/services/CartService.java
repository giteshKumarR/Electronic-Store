package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.payload.AddItemToCartRequest;

public interface CartService {
//    NOTE : UserID is important as Cart is associated to User....
    // 1. Add item to Cart
    // Request contains the information that how many quantity of
    // products needs to be added into the cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    // 2. remove Item for  cart
    void removeItemFromCart(String userId, Long cartItemId);

    // 3. Remove all items from the cart
    void removeAllFromCart(String userId);

    // 4. Search Cart by userID
    CartDto searchCartByUser(String userId);

}
