package com.Jas.shop_backend.model.DAO;

import com.Jas.shop_backend.model.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ProductDAO extends ListCrudRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);

    boolean existsByCategoryId(Long categoryId);

}
