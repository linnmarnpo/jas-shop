package com.Jas.shop_backend.Service;

import com.Jas.shop_backend.api.model.CartDto;
import com.Jas.shop_backend.api.model.CartRequest;
import com.Jas.shop_backend.exception.ResourceNotFoundException;
import com.Jas.shop_backend.model.*;
import com.Jas.shop_backend.model.DAO.CartDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final UserDetailsService userDetailsService;
    private final CartDAO cartDAO;
    private final ProductService productService;

    public CartService(UserDetailsService userDetailsService, CartDAO cartDAO, ProductService productService) {
        this.userDetailsService = userDetailsService;
        this.cartDAO = cartDAO;
        this.productService = productService;
    }

    public Cart createCart(CartRequest cartRequest, Principal principal) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        System.out.println(cartRequest.getProductId());
        System.out.println(cartRequest.getProductVariantId());
        Product product = productService.getProduct(cartRequest.getProductId());
        ProductVariant productVariant = product.getProductVariants().stream().filter(variant ->
                        variant.getId().equals(cartRequest.getProductVariantId())
                )
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invalid product variant"));

        Optional<Cart> existingCartOpt = cartDAO.findByUserAndProductVariant(user, productVariant);
        if(existingCartOpt.isPresent()) {
            Cart existingCart = existingCartOpt.get();
            existingCart.setQuantity(existingCart.getQuantity()+ cartRequest.getQuantity());
            return cartDAO.save(existingCart);
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setProductVariant(productVariant);
        cart.setQuantity(cartRequest.getQuantity());
        return cartDAO.save(cart);
    }

    public List<CartDto> getCarts(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        List<Cart> carts = cartDAO.findByUser(user);

        if(carts.isEmpty()) {
            throw new ResourceNotFoundException("Cart is Empty");
        }
        return carts.stream().map(cart -> {
            Product product = cart.getProduct();

            String imageUrl = product.getResources().stream()
                    .filter(r -> r.getColor()
                            .equals(cart.getProductVariant().getColor()))
                    .map(Resources::getUrl)
                    .findFirst()
                    .orElse(null);

            return new CartDto(
                    cart.getId(),
                    product.getId(),
                    product.getName(),
                    cart.getProductVariant(),
                    imageUrl,
                    product.getPrice(),
                    cart.getQuantity()
            );
        }).collect(Collectors.toList());
    }


    public void deleteCart(Long id) {
        Cart cart = cartDAO.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cart is not found"));
        cartDAO.deleteById(id);
    }
}
