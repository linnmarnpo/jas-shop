package com.Jas.shop_backend.model.DAO;

import com.Jas.shop_backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDAO extends JpaRepository<OrderItem, Long> {
    boolean existsByProductId(Long productId);
}
