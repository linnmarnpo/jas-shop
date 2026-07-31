package com.Jas.shop_backend.api.controller;

import com.Jas.shop_backend.Service.ProductService;
import com.Jas.shop_backend.api.model.ApiResponse;
import com.Jas.shop_backend.api.model.ProductDTO;
import com.Jas.shop_backend.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductDTO>> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.addProduct(productDTO);

        ApiResponse<ProductDTO> response = new ApiResponse<>(
                true,
                "Product created successfully",
                created
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updated = productService.updateProduct(id, productDTO);
        ApiResponse<ProductDTO> response = new ApiResponse<>(
                true,
                "Product updated successfully",
                updated
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        ApiResponse<Product> response = new ApiResponse<>(
                true,
                "Product fetched successfully",
                product
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getProducts(
            @RequestParam(required = false, name = "categoryId") Long categoryId
    ) {
        List<Product> products = productService.getProducts(categoryId);

        ApiResponse<List<Product>> response = new ApiResponse<>(
                true,
                "Product fetched successfully",
                products
        );

        return ResponseEntity.ok(response);
    }

}
