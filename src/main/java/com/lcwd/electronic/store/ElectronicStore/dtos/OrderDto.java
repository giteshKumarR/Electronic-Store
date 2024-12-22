package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.enums.OrderStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentMethod;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.ShippingMethod;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDto {
    private String orderId;
    private String OrderNumber;
    private UserDto user;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private Double totalBeforeTax;
    private Double tax;
    private Double shippingCost;
    private Double totalAmountAfterTax;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private AddressDto shippingAddress;
    private ShippingMethod shippingMethod;
    private AddressDto billingAddress;
    private PaymentMethod paymentMethod;
    private String paymentTransactionId;
    private PaymentStatus paymentStatus;
    private String orderNotes;
    private LocalDateTime estimatedDeliveryDate; // depends shipping method
    private LocalDateTime deliveredDate; // for now put +1 or estimated Delivery date
}
