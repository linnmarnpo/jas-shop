package com.Jas.shop_backend.api.model;

import com.Jas.shop_backend.model.ProductVariant;
import com.Jas.shop_backend.model.Resources;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull
    private Long categoryId;

    @NotNull(message = "Product variants required")
    private List<ProductVariant> productVariants;

    @NotNull(message = "Product resources required")
    private List<Resources> resources;

}
