package com.Jas.shop_backend.api.controller.auth;

import com.Jas.shop_backend.api.model.ApiResponse;
import com.Jas.shop_backend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserDetailController {

    private final UserDetailsService userDetailsService;

    public UserDetailController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUserProfile(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

//        if(user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        ApiResponse<User> response = new ApiResponse<>(
                true,
                "User fetched successfully",
                user
        );
        return ResponseEntity.ok(response);
    }
}
