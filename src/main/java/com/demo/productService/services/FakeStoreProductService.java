package com.demo.productService.services;

import com.demo.productService.dtos.FakeStoreProductDto;
import com.demo.productService.exceptions.ProductNotFoundException;
import com.demo.productService.models.Category;
import com.demo.productService.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("fakeStoreProductServcie")
public class FakeStoreProductService implements ProductService {

    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {

        ResponseEntity<FakeStoreProductDto> forEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/" + productId, FakeStoreProductDto.class);
        FakeStoreProductDto fakeStoreProductDto = forEntity.getBody();
        if(fakeStoreProductDto == null){
            throw new ProductNotFoundException(productId);
        }
        return convertFaleStorePrductDtoToProduct(fakeStoreProductDto);
    }

    private Product convertFaleStorePrductDtoToProduct(FakeStoreProductDto fakeStoreProductDto){
        Product product = new Product();
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImageUrl());
        product.setId(fakeStoreProductDto.getId());

        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;

    }

    public FakeStoreProductDto convertProductToFakeStoreProductDto(Product product){
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImageUrl(product.getImageUrl());
        if(product.getCategory() != null) {
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        return fakeStoreProductDto;
    }

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<FakeStoreProductDto[]> forEntity = restTemplate.getForEntity("https://fakestoreapi.com/products", FakeStoreProductDto[].class);
        FakeStoreProductDto[] fakeStoreProductDtos = forEntity.getBody();
        if(fakeStoreProductDtos != null){
            return  List.of(fakeStoreProductDtos).stream()
                    .map(this::convertFaleStorePrductDtoToProduct)
                    .toList();
        }


        return List.of();
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDto fakeStoreProductDto = convertProductToFakeStoreProductDto(product);
        ResponseEntity<FakeStoreProductDto> forEntity = restTemplate.postForEntity("https://fakestoreapi.com/products", fakeStoreProductDto, FakeStoreProductDto.class);
        FakeStoreProductDto createdFakeStoreProductDto = forEntity.getBody();
        if(createdFakeStoreProductDto != null){
            return convertFaleStorePrductDtoToProduct(createdFakeStoreProductDto);
        }
        return null;

    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        FakeStoreProductDto fakeStoreProductDto = convertProductToFakeStoreProductDto(product);
        restTemplate.put("https://fakestoreapi.com/products/"+ productId, fakeStoreProductDto);
            return convertFaleStorePrductDtoToProduct(fakeStoreProductDto);
    }

    @Override
    public void deleteProduct(Long productId) {
        restTemplate.delete("https://fakestoreapi.com/products/" + productId);
    }

    public Product updateProduct(Long productId,  java.util.Map<String, Object> updates) throws ProductNotFoundException {
        // Fetch the existing product
        Product existingProduct = getSingleProduct(productId);
        if (existingProduct == null) {
            return null;
        }

        // Apply updates
        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> existingProduct.setTitle((String) value);
                case "price" -> existingProduct.setPrice(Double.valueOf(value.toString()));
                case "description" -> existingProduct.setDescription((String) value);
                case "imageUrl" -> existingProduct.setImageUrl((String) value);
                case "category" -> {
                    Category category = new Category();
                    category.setName((String) value);
                    existingProduct.setCategory(category);
                }
            }
        });

        // Replace the product with updated values
        return replaceProduct(productId, existingProduct);
    }

    @Override
    public boolean validateTokenIfAnyOtherServiceCalls(String token) {
        return false;
    }

    @Override
    public Page<Product> getProductsByTitle(String title, int pageNumber, int pageSize) {
        return null;
    }

}
