package com.demo.productService.controllers;

import com.demo.productService.exceptions.InvalidCategoryException;
import com.demo.productService.exceptions.InvalidProductException;
import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Category;
import com.demo.productService.models.Product;
import com.demo.productService.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    public void testGetSingleProductPositive() throws ProductNotFoundException {
        //Arrange
        Long productId = 9L;

        Product expectedOutput = new Product();
        expectedOutput.setId(productId);
        expectedOutput.setTitle("iphone 15");
        expectedOutput.setDescription("Best ever phone");
        expectedOutput.setImageUrl("mobile.jpg");
        expectedOutput.setPrice(150000.00);
        Category category = new Category();
        category.setId(1L);
        category.setName("Mobile");
        expectedOutput.setCategory(category);

        when(productService.getSingleProduct(productId)).thenReturn(expectedOutput);

        //Act
        Product actualProduct = productController.getSingleProduct(productId);

        //Assert
        assertEquals(expectedOutput, actualProduct, "Products are not same");

    }

    @Test
    public void  testGetSingleProductInvalidId() throws ProductNotFoundException {
        Long productId = -1L;

        when(productService.getSingleProduct(productId)).
                thenThrow(new ProductNotFoundException());

        assertThrows(ProductNotFoundException.class, () -> productController.getSingleProduct(productId));
    }

    @Test
    void testGetSingleProduct_EdgeCase_LargeId() throws ProductNotFoundException {
        Long productId = Long.MAX_VALUE;

        when(productService.getSingleProduct(productId))
                .thenThrow(new ProductNotFoundException("Product not found for large ID"));

        ProductNotFoundException exception =assertThrows(ProductNotFoundException.class,
                () -> productController.getSingleProduct(productId));

        assertEquals("Product not found for large ID", exception.getMessage());
    }

    @Test
    public void testGetAllProducts_Positive(){
        //Arrange List of products
        Long id1 = 1L;
        Product p1 = new Product();
        p1.setId(id1);
        p1.setTitle("iphone 15");
        p1.setDescription("Best ever phone");
        p1.setImageUrl("mobile.jpg");
        p1.setPrice(150000.00);
        Category category = new Category();
        category.setId(1L);
        category.setName("Mobile");
        p1.setCategory(category);

        Long id2 = 2L;
        Product p2 = new Product();
        p2.setId(id2);
        p2.setTitle("iphone 15");
        p2.setDescription("Best ever phone");
        p2.setImageUrl("mobile.jpg");
        p2.setPrice(150000.00);
        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Mobile");
        p2.setCategory(category2);

        Long id3 = 3L;
        Product p3 = new Product();
        p3.setId(id1);
        p3.setTitle("iphone 15");
        p3.setDescription("Best ever phone");
        p3.setImageUrl("mobile.jpg");
        p3.setPrice(150000.00);
        Category category3 = new Category();
        category3.setId(1L);
        category3.setName("Mobile");
        p3.setCategory(category3);

        List<Product> expectedproductList = List.of(p1,p2,p3);

        when(productController.getAllProducts()).thenReturn(expectedproductList);

        List<Product> actualProductsList = productController.getAllProducts();

        assertEquals(expectedproductList, actualProductsList);
        assertEquals(3, actualProductsList.size(), "Expected 3 products");
        assertNotNull(actualProductsList,"Product list should not be null");

    }

    @Test
    public void testGetAllProducts_Negative(){

        when(productService.getAllProducts()).thenThrow(new RuntimeException("Database connection error"));
        assertThrows(RuntimeException.class, () -> productController.getAllProducts());

    }
    @Test
    public void testGetAllProducts_EdgeCase_EmptyList(){
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());
       List<Product> actualProduct =  productController.getAllProducts();

       assertTrue(actualProduct.isEmpty(),"Expected empty list");
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setTitle("Running Shoes");
        expectedProduct.setDescription("Lightweight running shoes for men and women");
        expectedProduct.setPrice(2499.00);
        expectedProduct.setImageUrl("https://example.com/images/shoes.jpg");

        Category c = new Category();
        c.setId(1L);
        c.setName("Sports");
        expectedProduct.setCategory(c);

        when(productService.createProduct(any(Product.class))).thenReturn(expectedProduct);
        ResponseEntity<Product> actualProduct = productController.createProduct(expectedProduct);
        assertEquals(HttpStatus.CREATED, actualProduct.getStatusCode());
        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getTitle(), actualProduct.getBody().getTitle());
        assertEquals(expectedProduct.getCategory().getName(), actualProduct.getBody().getCategory().getName());

    }

    @Test
    public void testCreateProduct_InvalidProductException() throws InvalidProductException, InvalidCategoryException {

        Product p = new Product();
        when(productService.createProduct(p))
                .thenThrow(new InvalidProductException("Product is invalid. Title and price are required."));

        InvalidProductException exception = assertThrows(InvalidProductException.class, () -> productController.createProduct(p));
        assertEquals("Product is invalid. Title and price are required.", exception.getMessage());
    }

    @Test
    public void testCreateProduct_InvalidCategoryException() throws InvalidProductException, InvalidCategoryException {
        Product p = new Product();
        p.setTitle("Leather Wallet");
        p.setPrice(2000.00);
        p.setCategory(null);

        when(productService.createProduct(p)).thenThrow(new InvalidCategoryException("Category name is required"));

        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () -> productController.createProduct(p));
        assertEquals("Category name is required", exception.getMessage());
    }
    @Test
    public void testCreateProduct_ZeroPrice() throws InvalidProductException, InvalidCategoryException {

        Category category = new Category();
        category.setName("Fashion");

        Product product = new Product();
        product.setTitle("Leather Wallet");
        product.setPrice(-1.0);
        product.setDescription(""); // Empty description
        product.setCategory(category);

        when(productService.createProduct(product)).thenReturn(product);
        ResponseEntity<Product> response = productController.createProduct(product);

        assertEquals(-1.0, response.getBody().getPrice());

    }

    @Test
    public void testCreateProduct_VeryLongTitle() throws InvalidProductException, InvalidCategoryException {
        Category category = new Category();
        category.setName("Fashion");

        Product product = new Product();
        product.setTitle("Leather Wallet is nice product fdghjkljhgfdghjklhgfghfghjkhgfghjkhgfghjkhgdfghjkjhghfdcvccccccccbgvvvvvvvvvvv");
        product.setPrice(-1.0);
        product.setDescription(""); // Empty description
        product.setCategory(category);

        when(productService.createProduct(product)).thenReturn(product);
        ResponseEntity<Product> response = productController.createProduct(product);
        assertEquals(product.getTitle().length(), response.getBody().getTitle().length());

    }


}

