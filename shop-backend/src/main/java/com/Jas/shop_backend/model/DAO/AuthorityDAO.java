package com.Jas.shop_backend.model.DAO;

import com.Jas.shop_backend.enums.Role;
import com.Jas.shop_backend.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityDAO extends JpaRepository<Authority, Long> {
    Authority findByRole(Role role);
}
