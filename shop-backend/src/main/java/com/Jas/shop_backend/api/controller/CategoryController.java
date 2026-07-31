package com.Jas.shop_backend.api.controller;

import com.Jas.shop_backend.Service.CategoryService;
import com.Jas.shop_backend.api.model.ApiResponse;
import com.Jas.shop_backend.api.model.ProductDTO;
import com.Jas.shop_backend.model.Category;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    public final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Category>> addCategory(@Valid @RequestBody Category category) {
        Category created = categoryService.addCategory(category);

        ApiResponse<Category> response = new ApiResponse<>(
                true,
                "A new category is created",
                created
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Category>> deleteCategory(@PathVariable Long id) {
        Category deleted = categoryService.deleteCategory(id);
        ApiResponse<Category> response = new ApiResponse<>(
                true,
                "Category is deleted",
                deleted
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        List<Category> categories = categoryService.getCategories();

        ApiResponse<List<Category>> response = new ApiResponse<>(
                true,
                "Category fetched successfully",
                categories
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<Category>> editCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        Category updated = categoryService.editCategory(id, category);
        ApiResponse<Category> response = new ApiResponse<>(
                true,
                "Category updated successfully",
                updated
        );

        return ResponseEntity.ok(response);
    }

}
