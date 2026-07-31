package com.Jas.shop_backend.api.model;

import lombok.Getter;

@Getter
public class CartRequest {
    private Long userId;
    private Long productId;
    private Long productVariantId;
    private Integer quantity;
}
