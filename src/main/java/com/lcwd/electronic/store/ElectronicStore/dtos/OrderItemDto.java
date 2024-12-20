package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Order;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;

public class OrderItemDto {
    private Long orderItemId;
    private Long quantity;
    private Double totalPrice;
    private Product product;
    private Order order;
}
