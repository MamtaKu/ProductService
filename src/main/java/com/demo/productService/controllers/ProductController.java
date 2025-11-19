package com.demo.productService.controllers;

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
    public Product createProduct(@RequestBody Product product){
        return productService.createProduct(product);

    }

    @PutMapping("{productId}")
    public Product replaceProduct(@PathVariable("productId") Long productId, @RequestBody Product product){
        return productService.replaceProduct(productId, product);
    }

    @DeleteMapping("{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
    }

    @PatchMapping("{productId}")
    public Product updateProduct(@PathVariable("productId") Long productId, @RequestBody Map<String, Object> updates) throws ProductNotFoundException {
       return productService.updateProduct(productId, updates);
    }

}
