package com.example.cash_flow_backend.security.service;

import com.example.cash_flow_backend.security.UserException;
import com.example.cash_flow_backend.security.model.Role;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder encoder;
    private JwtService jwtService;
    private UserRepo userRepo;

    public SecurityService(AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder, JwtService jwtService, UserRepo userRepo) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> saveUser(User user) throws UserException {
        Optional<User> u = this.userRepo.findByUsername(user.getUsername());
        if (u.isPresent()){
            throw new UserException("The username already exists " + user.getUsername());
        }
        user.setPassword(this.encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
    }

    public ResponseEntity<?> login(User user) {
    }
}
