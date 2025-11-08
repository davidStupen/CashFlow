package com.example.cash_flow_backend.security.service;

import com.example.cash_flow_backend.security.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public String generateToken(User user){

    }
    public String extractUsername(String token) {

    }

    public boolean validationToken(String token, UserDetails userDetails) {

    }
}
