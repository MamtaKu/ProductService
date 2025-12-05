package com.demo.productService.services;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.demo.productService.dtos.UserDto;
import com.demo.productService.exceptions.*;
import com.demo.productService.models.Category;
import com.demo.productService.models.Product;
import com.demo.productService.repositories.CategoryRepository;
import com.demo.productService.repositories.ProductRepository;
import com.demo.productService.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

//@Slf4j
@Service("realDatabaseProductService")
public class RealDatabaseProductService implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private RestTemplate restTemplate;
    private RedisTemplate<String, Object> redisTemplate;


//    private static final String HASH_KEY = "PRODUCTS";
//    private static final String FIELD_PREFIX = "PRODUCTS_";
//    private static final Logger log = LoggerFactory.getLogger(RealDatabaseProductService.class);
//

    public RealDatabaseProductService(ProductRepository productRepository, CategoryRepository categoryRepository, RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {

        //first check if the product with Id is present in redis cache.
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCTS_" + productId);
        if(product != null){
            //return from cache
            return product;
        }


        Optional<Product> optional = productRepository.findById(productId);
        if(optional.isEmpty()){
            throw new ProductNotFoundException(productId);
        }
        //Before returning the product, store it in redis cache
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCTS_"+ productId, optional.get());
        return optional.get();

//        final String field = FIELD_PREFIX + productId;
//        Product cached = null;
//
//        // -------- Cache GET (fail open) --------
//        try {
//            cached = (Product) redisTemplate.opsForHash().get(HASH_KEY, field);
//            if (cached != null) {
//                log.debug("Cache HIT for productId={}", productId);
//                return cached;
//            }
//
//            log.debug("Cache MISS for productId={}", productId);
//        } catch (RedisConnectionFailureException e) {
//            log.warn("Redis connection error on GET for productId={}: {}", productId, e.getMessage());
//        } catch (SerializationException e) {
//            log.warn("Redis serialization error on GET for productId={}: {}", productId, e.getMessage());
//        } catch (DataAccessException e) {
//            log.warn("Redis data access error on GET for productId={}: {}", productId, e.getMessage());
//        }
//        // -------- Database GET --------
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException(productId));
//
//        // -------- Cache PUT (best-effort) --------
//        try {
//            redisTemplate.opsForHash().put(HASH_KEY, field, product);
//            // Optional: if you want a TTL for the WHOLE hash:
//            // redisTemplate.expire(HASH_KEY, Duration.ofMinutes(10));
//            log.debug("Cached productId={} into Redis hash={}", productId, HASH_KEY);
//        } catch (RedisConnectionFailureException e) {
//            log.warn("Redis connection error on PUT for productId={}: {}", productId, e.getMessage());
//        } catch (SerializationException e) {
//        log.warn("Redis serialization error on PUT for productId={}: {}", productId, e.getMessage());
//    } catch (DataAccessException e) {
//        log.warn("Redis data access error on PUT for productId={}: {}", productId, e.getMessage());
//    }
//
//        return product;

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


    public boolean validateTokenIfAnyOtherServiceCalls(String tokenValue) throws InvalidTokenException {
        String url = "http://localhost:8080/users/validateToken/" + tokenValue;

        try {
            ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(url, UserDto.class);
            System.out.println(responseEntity);
            UserDto body = responseEntity.getBody();
            if (body != null) {
                return true;
            }
            return false;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                throw new InvalidTokenException("Token is invalid or expired");
            }
        }
        return false;
    }

    @Override
    public Page<Product> getProductsByTitle(String title, int pageNumber, int pageSize) {
        Sort sort = Sort.by("price").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findByTitleContainsIgnoreCase(title, pageRequest);

    }

}
