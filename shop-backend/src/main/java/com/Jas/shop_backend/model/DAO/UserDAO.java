package com.Jas.shop_backend.model.DAO;

import com.Jas.shop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByEmailAndResetPasswordToken(String email, String resetPasswordToken);

    boolean existsByName(String name);
}
