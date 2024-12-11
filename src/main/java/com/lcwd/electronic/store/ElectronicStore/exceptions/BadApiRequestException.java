package com.lcwd.electronic.store.ElectronicStore.exceptions;

public class BadApiRequestException extends RuntimeException{
    public BadApiRequestException() {
        super("Bad request");
    }
    public BadApiRequestException(String message) {
        super(message);
    }
}
