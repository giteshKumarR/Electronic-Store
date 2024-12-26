package com.lcwd.electronic.store.ElectronicStore.enums;

public enum OrderStatus {
    PENDING, // means order is in cart but not yet confirmed
    CONFIRMED, // means order's payment is the next step
    PROCESSING, // means after payment preparations are being done
    SHIPPED, // order is packed and shipped to depot
    OUT_FOR_DELIVERY, // order out for delivery
    DELIVERED, // order Delivered
    CANCELLED, // Order cancellation
    REFUNDED, // Order amount refunded
    RETURNED // Order returned
}
