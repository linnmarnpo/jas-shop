package com.Jas.shop_backend.specification;

import com.Jas.shop_backend.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return ((root, query, criteriaBuilder) ->
                categoryId == null ? null :
                criteriaBuilder.equal(root.get("category").get("id"),categoryId));
    }

}
