package com.lcwd.electronic.store.ElectronicStore.exceptions.cartexceptions;

public class MoreQuantityThanStockException extends RuntimeException{
    public MoreQuantityThanStockException() {
        super("More quantity than stock!!");
    }
    public MoreQuantityThanStockException(String message) {
        super(message);
    }
}
