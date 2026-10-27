package com.uretek.uretek_inventory.security;

import com.uretek.uretek_inventory.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String ADMIN_USERNAME = "admin";
    // This is bcrypt hash of "uretek123" — generated once, hardcoded
    private final String ADMIN_PASSWORD_HASH = "$2a$10$YuBTL3pxmSuOo39o3fZmZO8cL/PsZSSashzrcFOsnIIYHMDvqLAJy";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getUsername().equals(ADMIN_USERNAME) &&
                passwordEncoder.matches(request.getPassword(), ADMIN_PASSWORD_HASH)) {

            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }


}