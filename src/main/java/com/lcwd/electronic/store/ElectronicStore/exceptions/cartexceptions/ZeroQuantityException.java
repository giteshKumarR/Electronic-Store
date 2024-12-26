package com.lcwd.electronic.store.ElectronicStore.exceptions.cartexceptions;

public class ZeroQuantityException extends RuntimeException{
    public ZeroQuantityException() {
        super("Quantity is zero!!");
    }
    public ZeroQuantityException(String message) {
        super(message);
    }
}
