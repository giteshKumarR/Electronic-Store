package com.lcwd.electronic.store.ElectronicStore.exceptions;

public class CannotChangeEmailException extends RuntimeException {
    public CannotChangeEmailException() {
        super("Cannot update email !!");
    }
    public CannotChangeEmailException(String cannotChangeEmail) {
        super(cannotChangeEmail);
    }
}
