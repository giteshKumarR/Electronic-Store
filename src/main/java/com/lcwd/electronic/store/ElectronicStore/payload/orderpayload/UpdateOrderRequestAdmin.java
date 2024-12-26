package com.lcwd.electronic.store.ElectronicStore.payload.orderpayload;

import com.lcwd.electronic.store.ElectronicStore.enums.OrderStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateOrderRequestAdmin {
    private String orderId;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
}
