package com.Jas.shop_backend.api.model;

import com.Jas.shop_backend.model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long productId;
    private String productName;
    private ProductVariant productVariant;
    private String imageUrl;
    private Double price;
    private Integer quantity;
}
