package com.lcwd.electronic.store.ElectronicStore.exceptions;

import lombok.Builder;

@Builder
public class ResourseNotFoundException extends  RuntimeException{
    public ResourseNotFoundException() {
        super("Resource Not Found !!");
    }
    public ResourseNotFoundException(String message) {
        super(message);
    }
}
