package com.Jas.shop_backend.Service;

import com.Jas.shop_backend.enums.Role;
import com.Jas.shop_backend.model.Authority;
import com.Jas.shop_backend.model.DAO.AuthorityDAO;

import java.util.ArrayList;
import java.util.List;

public class AuthorityService {

    public final AuthorityDAO authorityDAO;

    public AuthorityService(AuthorityDAO authorityDAO) {
        this.authorityDAO = authorityDAO;
    }

    public List<Authority> getAuthority() {
        List<Authority> authorities = new ArrayList<>();
        Authority authority = authorityDAO.findByRole(Role.USER);
        authorities.add(authority);
        return authorities;
    }

}
