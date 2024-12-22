package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddressDto {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String phoneNbr;
    private String recipientName;
}
