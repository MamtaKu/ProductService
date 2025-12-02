package com.demo.productService.exceptions;

public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
