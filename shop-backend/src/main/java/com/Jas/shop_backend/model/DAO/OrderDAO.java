package com.Jas.shop_backend.model.DAO;

import com.Jas.shop_backend.model.Order;
import com.Jas.shop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}
