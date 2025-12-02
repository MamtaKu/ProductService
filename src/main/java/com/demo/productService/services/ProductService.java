package com.demo.productService.services;

import com.demo.productService.exceptions.InvalidCategoryException;
import com.demo.productService.exceptions.InvalidProductException;
import com.demo.productService.exceptions.InvalidTokenException;
import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Product getSingleProduct(Long productId) throws ProductNotFoundException;
    List<Product> getAllProducts();
    Product createProduct(Product product) throws InvalidProductException, InvalidCategoryException;
    Product replaceProduct(Long productId, Product product) throws ProductNotFoundException, InvalidCategoryException;
    void deleteProduct(Long productId) throws ProductNotFoundException;
    Product updateProduct(Long productId,  Map<String, Object> updates) throws ProductNotFoundException;
    boolean validateTokenIfAnyOtherServiceCalls(String token) throws InvalidTokenException;
    Page<Product> getProductsByTitle(String title, int pageNumber, int pageSize);
}

