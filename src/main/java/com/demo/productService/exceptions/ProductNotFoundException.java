package com.demo.productService.exceptions;

public class ProductNotFoundException extends Exception {
    private Long productId;
    public ProductNotFoundException(Long productId){
        this.productId = productId;
    }
    public ProductNotFoundException(String s){
        super(s);
    }
    public ProductNotFoundException(){
    }



    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
