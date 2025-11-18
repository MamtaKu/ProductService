package com.demo.productService.repositories;

import com.demo.productService.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositories extends JpaRepository<Product, Long> {


}
