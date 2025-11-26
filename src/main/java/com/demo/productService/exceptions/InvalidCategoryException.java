package com.demo.productService.exceptions;

public class InvalidCategoryException extends Exception {
    public InvalidCategoryException(String msg){
        super(msg);
    }
}
