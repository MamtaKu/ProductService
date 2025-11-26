package com.demo.productService.exceptions;

public class InvalidProductException extends Exception {
    public InvalidProductException(String msg){
        super(msg);
    }
}
