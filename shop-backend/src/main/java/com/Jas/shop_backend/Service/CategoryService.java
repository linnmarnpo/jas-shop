package com.Jas.shop_backend.Service;

import com.Jas.shop_backend.exception.ApiConflictException;
import com.Jas.shop_backend.exception.ResourceNotFoundException;
import com.Jas.shop_backend.model.Category;
import com.Jas.shop_backend.model.DAO.CategoryDAO;
import com.Jas.shop_backend.model.DAO.ProductDAO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final ProductDAO productDAO;

    public CategoryService(CategoryDAO categoryDAO, ProductDAO productDAO) {
        this.categoryDAO = categoryDAO;
        this.productDAO = productDAO;
    }

    public Category addCategory(Category category) {
        if (categoryDAO.existsByName(category.getName())) {
            throw new ApiConflictException("Category already exists");
        }
        return categoryDAO.save(category);
    }

    public Category deleteCategory(Long id) {

        Category category = categoryDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        if( productDAO.existsByCategoryId(id) ) {
            throw new ApiConflictException("Can't delete category with existing products");
        }

        categoryDAO.deleteById(id);
        return category;
    }

    public List<Category> getCategories() {
        return categoryDAO.findAll();
    }

    public Category editCategory(Long id, Category updateCategory) {

        Category category = categoryDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        category.setName(updateCategory.getName());
        category.setDescription(updateCategory.getDescription());
        category.setImageUrl(updateCategory.getImageUrl());
        return categoryDAO.save(category);
    }
}
