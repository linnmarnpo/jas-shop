package com.Jas.shop_backend.api.controller;

import com.Jas.shop_backend.Service.CartService;
import com.Jas.shop_backend.api.model.CartDto;
import com.Jas.shop_backend.api.model.CartRequest;
import com.Jas.shop_backend.model.Cart;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    public final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> getCarts(Principal principal) throws Exception{
        List<CartDto> carts = cartService.getCarts(principal);
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@Valid @RequestBody CartRequest cartRequest, Principal principal) throws Exception{
        Cart cart = cartService.createCart(cartRequest, principal);

        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
