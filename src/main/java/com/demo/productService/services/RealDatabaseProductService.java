package com.demo.productService.services;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.demo.productService.exceptions.InvalidCategoryException;
import com.demo.productService.exceptions.InvalidProductException;
import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Category;
import com.demo.productService.models.Product;
import com.demo.productService.repositories.CategoryRepository;
import com.demo.productService.repositories.ProductRepository;
import com.demo.productService.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

@Service("realDatabaseProductService")
public class RealDatabaseProductService implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public RealDatabaseProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> optional = productRepository.findById(productId);
        if(optional.isEmpty()){
            throw new ProductNotFoundException(productId);
        }
        return optional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        }
        catch(DataAccessException e){
            throw new RuntimeException("Database connection failed",e);
        }

    }

    @Override
    public Product createProduct(Product product) throws InvalidCategoryException, InvalidProductException {
        //validate Product object
        if(product == null || product.getTitle() == null ||product.getTitle().isEmpty() || product.getPrice() == null){
            throw new InvalidProductException("Product is invalid. Title and price are required.");
        }

        //check price is not zero or negative
        if(product.getPrice() <=0.0){
            throw new InvalidProductException("Price can not be zero and negative");
        }

        //check title length is not more than 50
        if(product.getTitle().length() > 255){
            throw new InvalidProductException("Product Title is very long");
        }
        //validate Category
        Category category = product.getCategory();
        if(category == null || category.getName() == null ||category.getName().isBlank()){
            throw new InvalidCategoryException("Category name is required");
        }
        //check if category exists, else create new
        Optional<Category> optional = categoryRepository.findByName(category.getName());
        if(optional.isEmpty()){
            Category saved = categoryRepository.save(category);
            product.setCategory(saved);
        }
        else{
            product.setCategory(optional.get());
        }

        //save product
       return productRepository.save(product);
    }

    @Override
    public Product replaceProduct(Long productId, Product product) throws ProductNotFoundException, InvalidCategoryException {
        Product existingProduct = productRepository.findById((productId)).orElseThrow(() -> new ProductNotFoundException("Product Not found"));

            existingProduct.setTitle(product.getTitle());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setImageUrl(product.getImageUrl());
            if(product.getCategory() == null || product.getCategory().getName().isBlank()){
                throw new InvalidCategoryException("Category name is required");
            }
            Optional<Category> optionalCategory = categoryRepository.findByName(product.getCategory().getName());
            if(optionalCategory.isEmpty()) {
                Category savedCategory = categoryRepository.save(product.getCategory());
                existingProduct.setCategory(savedCategory);
            }
            else{
                existingProduct.setCategory(optionalCategory.get());
            }
        return productRepository.save(existingProduct);

    }

    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException {
        Product existingProduct = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Prodcut not existing with this productId "+ productId));

        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Long productId, Map<String, Object> updates) throws ProductNotFoundException {
        System.out.println("DEBUG");
        return null;
    }

}
