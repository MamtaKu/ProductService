package com.demo.productService.controllers;

import com.demo.productService.exceptions.InvalidCategoryException;
import com.demo.productService.exceptions.InvalidProductException;
import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Product;
import com.demo.productService.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("realDatabaseProductService") ProductService productService){
        this.productService = productService;
    }

    @GetMapping("{productId}")
    public Product getSingleProduct(@PathVariable("productId") Long productId) throws ProductNotFoundException {
        return productService.getSingleProduct(productId);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();

    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) throws InvalidProductException, InvalidCategoryException {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);


    }

    @PutMapping("{productId}")
    public ResponseEntity<Product> replaceProduct(@PathVariable("productId") Long productId, @RequestBody Product product) throws ProductNotFoundException, InvalidCategoryException {
        Product replacedProduct = productService.replaceProduct(productId, product);
        return new ResponseEntity<>(replacedProduct, HttpStatus.OK);
    }

    @DeleteMapping("{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) throws ProductNotFoundException {
        productService.deleteProduct(productId);
        return "Product with ID " + productId + " has been deleted.";
    }

    @PatchMapping("{productId}")
    public Product updateProduct(@PathVariable("productId") Long productId, @RequestBody Map<String, Object> updates) throws ProductNotFoundException {
       return productService.updateProduct(productId, updates);
    }

}
