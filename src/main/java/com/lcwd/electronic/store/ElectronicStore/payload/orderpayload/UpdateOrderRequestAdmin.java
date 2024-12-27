package com.lcwd.electronic.store.ElectronicStore.payload.orderpayload;

import com.lcwd.electronic.store.ElectronicStore.enums.OrderStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateOrderRequestAdmin {
    @NotBlank(message = "Order Id cannot be blank !!")
    private String orderId;
    private PaymentStatus paymentStatus;
    private OrderStatus status;
}
