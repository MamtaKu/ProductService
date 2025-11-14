package com.demo.productService.services;

import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Product getSingleProduct(Long productId) throws ProductNotFoundException;
    List<Product> getAllProducts();
    Product createProduct(Product product);
    Product replaceProduct(Long productId, Product product);
    void deleteProduct(Long productId);
    Product updateProduct(Long productId,  Map<String, Object> updates) throws ProductNotFoundException;
}
