package com.Jas.shop_backend.model;

import com.Jas.shop_backend.enums.Role;
import jakarta.persistence.*;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, unique = true)
    private Role role;

    @Override
    public @Nullable String getAuthority() {
        return "ROLE_" + role.name();
    }
}