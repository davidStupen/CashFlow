package com.example.cash_flow_backend.security.model.dto;

import com.example.cash_flow_backend.security.model.Role;

public record UserTokenDTO(
        String username,
        Role role,
        int userId
) {
}
