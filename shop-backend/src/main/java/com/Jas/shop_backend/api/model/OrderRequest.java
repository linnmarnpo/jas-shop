package com.Jas.shop_backend.api.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderRequest {

    private Long userId;
    private String address;
    private Double discount;
    private String paymentMethod;
}
