package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private String cartId;
    private UserDto userDto;
    private List<CartItemDto> cartItemDtoList;
    private Double totalCartPrice;
    private Integer totalCartItems;
}
