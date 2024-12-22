package com.lcwd.electronic.store.ElectronicStore.payload.orderpayload;

import com.lcwd.electronic.store.ElectronicStore.entities.Address;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentMethod;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateOrderRequestUser {
    private String orderId;
    private Address shippingAddress;
    private PaymentMethod paymentMethod;
    private String orderNotes;
}
