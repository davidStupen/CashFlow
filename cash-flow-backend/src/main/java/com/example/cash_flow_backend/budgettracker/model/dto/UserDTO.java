package com.example.cash_flow_backend.budgettracker.model.dto;

public record UserDTO(
        int id,
        String username,
        String role,
        String email
) {
}
