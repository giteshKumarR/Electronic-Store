package com.lcwd.electronic.store.ElectronicStore.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemToCartRequest {
    // This will contain all the information that needs to be passed to our cartService method
    // for adding the cartItem in the cart.
    private String productId;
    private Integer quantity;
}
