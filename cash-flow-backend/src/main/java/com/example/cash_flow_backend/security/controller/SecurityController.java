package com.example.cash_flow_backend.security.controller;

import com.example.cash_flow_backend.security.exeption.UserException;
import com.example.cash_flow_backend.security.model.Role;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.model.dto.PostUserDTO;
import com.example.cash_flow_backend.security.service.SecurityService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityController {
    private SecurityService securityService;
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/api/auth/registry")
    public ResponseEntity<?> registryUser(@RequestPart PostUserDTO user, @RequestPart(required = false) MultipartFile img){
        try {
            return this.securityService.registry(user, img);
        } catch (UserException | DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to create file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("email " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/api/admin/registry")
    public ResponseEntity<?> registryAdmin(@RequestPart PostUserDTO admin, @RequestPart(required = false) MultipartFile img){
        admin.setRole(Role.ROLE_ADMIN);
        try {
            return this.securityService.registry(admin, img);
        } catch (UserException | DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to create file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("email " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody PostUserDTO user){
        try {
            return this.securityService.login(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
