package com.globalblue.taxrefund.controller;

import com.globalblue.taxrefund.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public Map<String, String> generateToken(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String role = request.get("role");

        if (username == null || role == null) {
            throw new IllegalArgumentException("username and role are required");
        }

        String token = jwtUtil.generateToken(username, role);

        return Map.of(
                "token", token,
                "type", "Bearer"
        );
    }
}