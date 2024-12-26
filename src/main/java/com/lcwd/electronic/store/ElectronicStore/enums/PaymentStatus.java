package com.lcwd.electronic.store.ElectronicStore.enums;

public enum PaymentStatus {
    PAID, // only then payment transaction id and payment mode is updated
    NOT_PAID // payment transaction id and payment mode is updated will be set to null
}
