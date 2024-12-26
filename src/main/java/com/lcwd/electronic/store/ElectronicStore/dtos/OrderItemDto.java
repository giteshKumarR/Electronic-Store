package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemDto {
    private Long orderItemId;
    private Long quantity;
    private Double totalPrice;
    private ProductDto product;
}
