package com.lcwd.electronic.store.ElectronicStore.exceptions.orderexceptions;

public class NoOrderException extends RuntimeException{
    public NoOrderException() {
        super("No Orders !!");
    }
    public NoOrderException(String message) {
        super(message);
    }
}
