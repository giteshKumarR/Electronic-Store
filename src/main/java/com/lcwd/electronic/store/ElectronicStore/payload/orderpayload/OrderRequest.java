package com.lcwd.electronic.store.ElectronicStore.payload.orderpayload;

import com.lcwd.electronic.store.ElectronicStore.entities.Address;
import com.lcwd.electronic.store.ElectronicStore.enums.ShippingMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderRequest {
//    private String cartId;  ---> I think it is not required as using the
    // userId only I can get the cart for the user.
    @NotBlank(message = "Cart Id is required !!")
    private Address shippingAddress;

    @NotBlank(message = "Billing address is required !!")
    private Address billingAddress;

    @NotBlank(message = "Shipping Address is required !!")
    private ShippingMethod shippingMethod;
    private String paymentMethod;
    private String orderNotes;
}
