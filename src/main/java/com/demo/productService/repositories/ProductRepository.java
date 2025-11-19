package com.demo.productService.repositories;

import com.demo.productService.models.Product;
import com.demo.productService.projections.ProductWithTitleAndPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    Optional<Product> findById(Long aLong); //select * from Products where id = ?

    @Override
    List<Product> findAll(); //select * from products;

    //select * from Products where title = ?
    List<Product> findByTitle(String string);

    //select * from Products where title like %string%;
    List<Product> findByTitleContains(String string);

    //select * from products where price>=x and price <=y;
    List<Product> findByPriceBetween(Double x, Double y);

    @Override
    void deleteById(Long aLong);

    Product save(Product product);

    @Query(value = "select p.title, p.price from products p where p.id = 9", nativeQuery = true)
    List<ProductWithTitleAndPrice> findTitleAndPriceById();


    @Query("SELECT p.title AS title, p.price AS price FROM Product p")
    List<ProductWithTitleAndPrice> findTitleAndPrice();

}
