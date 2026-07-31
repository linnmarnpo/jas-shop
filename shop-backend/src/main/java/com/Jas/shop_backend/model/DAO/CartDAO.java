package com.Jas.shop_backend.model.DAO;

import com.Jas.shop_backend.model.Cart;
import com.Jas.shop_backend.model.ProductVariant;
import com.Jas.shop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartDAO extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndProductVariant(User user, ProductVariant productVariant);

    List<Cart> findByUser(User user);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
