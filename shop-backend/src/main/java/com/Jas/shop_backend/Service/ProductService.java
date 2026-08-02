package com.Jas.shop_backend.Service;

import com.Jas.shop_backend.api.model.ProductDTO;
import com.Jas.shop_backend.exception.ApiConflictException;
import com.Jas.shop_backend.exception.ResourceNotFoundException;
import com.Jas.shop_backend.model.Category;
import com.Jas.shop_backend.model.DAO.CategoryDAO;
import com.Jas.shop_backend.model.DAO.OrderItemDAO;
import com.Jas.shop_backend.model.DAO.ProductDAO;
import com.Jas.shop_backend.model.Product;
import com.Jas.shop_backend.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    public final ProductDAO productDAO;
    public final CategoryDAO categoryDAO;
    public final OrderItemDAO orderItemDAO;

    public ProductService(ProductDAO productDAO, CategoryDAO categoryDAO, OrderItemDAO orderItemDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
        this.orderItemDAO = orderItemDAO;
    }

    public ProductDTO addProduct(ProductDTO productDTO) {

        if (productDAO.existsByName(productDTO.getName())) {
            throw new ApiConflictException("Product already exists");
        }

        Product product = new Product();

        mapDtoProduct(productDTO, product);

        // Variants
        if (productDTO.getProductVariants() != null) {

            // Clear existing variants
            product.getProductVariants().clear();

            productDTO.getProductVariants().forEach(variant -> {
                variant.setProduct(product);
                product.getProductVariants().add(variant);
            });
        }

        // Resources
        if (productDTO.getResources() != null) {

            product.getResources().clear();

            productDTO.getResources().forEach(resource -> {
                resource.setProduct(product);
                product.getResources().add(resource);
            });
        }

        Product saved = productDAO.save(product);

        return getProductDTO(saved);
    }

    public void deleteProduct(Long id) {
        Product product = productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product does not exist"));

        if (orderItemDAO.existsByProductId(id)) {
            product.setActive(false);
            productDAO.save(product);
        } else {
            productDAO.delete(product);
        }
    }
    /**
     * edit Product
     * @param id
     * @param productDTO
     * @return
     */
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        Product product = productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product does not exist"));

        // map basic fields
        mapDtoProduct(productDTO, product);

        // Update variants
        if (productDTO.getProductVariants() != null) {

            // Clear existing variants
            product.getProductVariants().clear();

            productDTO.getProductVariants().forEach(variant -> {
                variant.setProduct(product);
                product.getProductVariants().add(variant);
            });
        }

        if (productDTO.getResources() != null) {

            product.getResources().clear();

            productDTO.getResources().forEach(resource -> {
                resource.setProduct(product);
                product.getResources().add(resource);
            });
        }

        Product updated = productDAO.save(product);
        return getProductDTO(updated);
    }

    /**
     * get all products
     * @return all active products
     */
    public List<Product> getProducts(Long categoryId) {

        Specification<Product> productSpecification = Specification
                .where(ProductSpecification.isActive(true))
                .and(ProductSpecification.hasCategoryId(categoryId));

        return productDAO.findAll(productSpecification);
    }

    public Product getProduct(Long id) {
        return productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product does not exist"));
    }

    /**
     * @param product
     * @return all product with only category id
     */
    private ProductDTO getProductDTO (Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setActive(product.getActive());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setProductVariants(product.getProductVariants());
        productDTO.setResources(product.getResources());
        return productDTO;
    }

    /**
     * mapping Dto to productModel
     * @param productDTO
     * @param product
     */
    private void mapDtoProduct(ProductDTO productDTO, Product product) {

        Category category = categoryDAO.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category does not exist"));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        if (productDTO.getActive() != null) {
            product.setActive(productDTO.getActive());
        }
        product.setCategory(category);

    }

}
