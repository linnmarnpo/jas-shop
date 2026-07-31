package com.Jas.shop_backend.config;

import com.Jas.shop_backend.enums.Role;
import com.Jas.shop_backend.model.Authority;
import com.Jas.shop_backend.model.DAO.AuthorityDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAuthorities(AuthorityDAO authorityDAO) {
        return args -> {

            for (Role role : Role.values()) {

                if (authorityDAO.findByRole(role) == null) {

                    Authority authority = new Authority();
                    authority.setRole(role);

                    authorityDAO.save(authority);
                }
            }
        };
    }
}
