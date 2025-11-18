package com.demo.productService.services;

import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RealDatabaseProductService implements ProductService {

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public Product updateProduct(Long productId, Map<String, Object> updates) throws ProductNotFoundException {
        return null;
    }
}
