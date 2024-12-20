package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Address;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.enums.OrderStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.ShippingMethod;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private String orderId;
    private String OrderNumber;
    private User user;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private Double totalBeforeTax;
    private Double tax;
    private Double shippingCost;
    private Double totalAmountAfterTax;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Address shippingAddress;
    private ShippingMethod shippingMethod;
    private Address billingAddress;
    private String paymentMethod;
    private String paymentTransactionId;
    private PaymentStatus paymentStatus;
    private String orderNotes;
    private LocalDateTime estimatedDeliveryDate; // depends shipping method
    private LocalDateTime deliveredDate; // for now put +1 or estimated Delivery date

}
