package com.demo.productService;

import com.demo.productService.projections.ProductWithTitleAndPrice;
import com.demo.productService.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductServiceApplicationTests {

	@Autowired
	private ProductRepository productRepository;

//	@Test
//	void contextLoads() {
//	}

//	@Test
//	public void testQuery() {
//		List<ProductWithTitleAndPrice> productWithTitleAndPrices =
//				productRepository.findTitleAndPriceById();
//
//		for(ProductWithTitleAndPrice productWithTitleAndPrice : productWithTitleAndPrices){
//			System.out.println(productWithTitleAndPrice.getTitle() +" " + productWithTitleAndPrice.getPrice());
//		}
//	}

//	@Test
//	public void testQuery1(){
//		List<ProductWithTitleAndPrice> titleAndPrice = productRepository.findTitleAndPrice();
//
//		for(ProductWithTitleAndPrice productWithTitleAndPrice : titleAndPrice){
//			System.out.println(productWithTitleAndPrice.getTitle() +" "+ productWithTitleAndPrice.getPrice());
//		}
//
//	}
}
