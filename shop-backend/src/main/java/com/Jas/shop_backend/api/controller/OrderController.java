package com.Jas.shop_backend.api.controller;

import com.Jas.shop_backend.Service.OrderService;
import com.Jas.shop_backend.api.model.OrderRequest;
import com.Jas.shop_backend.api.model.UpdateOrderStatusRequest;
import com.Jas.shop_backend.enums.OrderStatus;
import com.Jas.shop_backend.model.Order;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrder(Principal principal) {
        List<Order> orders = orderService.getOrder(principal);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        return ResponseEntity.ok(
                orderService.updateOrder(id, request.getStatus())
        );
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest, Principal principal) throws Exception {
        orderService.createOrder(orderRequest, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
