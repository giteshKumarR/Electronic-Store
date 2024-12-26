package com.lcwd.electronic.store.ElectronicStore.exceptions.cartexceptions;

public class EmptyCartException extends RuntimeException{
    public EmptyCartException() {
        super("Empty Cart !!");
    }
    public EmptyCartException(String message) {
        super(message);
    }
}
