package com.demo.productService.ControllerAdvices;

import com.demo.productService.ProductServiceApplication;
import com.demo.productService.exceptions.InvalidCategoryException;
import com.demo.productService.exceptions.InvalidProductException;
import com.demo.productService.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProductServiceExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleRunTimeException(){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleBNullPointerException(){
        return new ResponseEntity<>("Please try again with valid product id", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException productNotFoundException){
        return new ResponseEntity<>("This is invalid productId " + productNotFoundException.getProductId() + " please provide the valid product id", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidProductException(InvalidProductException invalidProductException){
        Map<String, Object> map = new HashMap<>();
        map.put("error", "InvalidProductException");
        map.put("message", invalidProductException.getMessage());
        map.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCategoryException(InvalidCategoryException invalidCategoryException){
        Map<String, Object> map = new HashMap<>();
        map.put("error", "InvalidCategoryException");
        map.put("message", invalidCategoryException.getMessage());
        map.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }
}
