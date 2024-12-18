package com.lcwd.electronic.store.ElectronicStore.dtos;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long cartItemId;
    private ProductDto productDto;
    private Integer quantity;
    private Integer totalPrice;
//    private Cart cart;
}
