package com.Jas.shop_backend.model.DAO;

import com.Jas.shop_backend.model.Category;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CategoryDAO extends ListCrudRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    boolean existsByName(String name);
}
