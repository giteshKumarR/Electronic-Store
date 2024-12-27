package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Address {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String phoneNbr;
    private String recipientName;
}
