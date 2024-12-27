package com.lcwd.electronic.store.ElectronicStore.payload.orderpayload;

import com.lcwd.electronic.store.ElectronicStore.entities.Address;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateOrderRequestUser {
    @NotBlank(message = "Order Id cannot be blank !!")
    private String orderId;
    private Address shippingAddress;
    private PaymentMethod paymentMethod;
    private String orderNotes;
}
