package com.example.cash_flow_backend.security.controller;

import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class SecurityController {
    private SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/registry")
    public ResponseEntity<?> registry(@RequestPart User user, @RequestPart MultipartFile img){
        return this.securityService.saveUser(user, img);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        return this.securityService.login(user);
    }
}
