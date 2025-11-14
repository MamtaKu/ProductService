package com.demo.productService.controllers;

import com.demo.productService.models.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping("/{id}")
    public Category getSingleCategory(@PathVariable Long id){
        throw new RuntimeException("Something went wrong");
    }
}
