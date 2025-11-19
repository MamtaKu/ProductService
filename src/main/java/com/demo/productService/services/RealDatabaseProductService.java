package com.demo.productService.services;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Category;
import com.demo.productService.models.Product;
import com.demo.productService.repositories.CategoryRepository;
import com.demo.productService.repositories.ProductRepository;
import com.demo.productService.repositories.ProductRepository;
import org.springframework.stereotype.Service;

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
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        //validation required based business logic
//        if(product.getId() != null){
//            Optional<Product> optionalObj = productRepository.findById(product.getId());
//            if(optionalObj.isEmpty()){
//                throw new RuntimeException("Invalid product");
//            }
//        }
        Category category = product.getCategory();

        Optional<Category> optional = categoryRepository.findByName(category.getName());
        if(optional.isEmpty()){
            Category saved = categoryRepository.save(category);
            product.setCategory(saved);
        }

       return productRepository.save(product);
    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Long productId, Map<String, Object> updates) throws ProductNotFoundException {
        System.out.println("DEBUG");
        return null;
    }

}
