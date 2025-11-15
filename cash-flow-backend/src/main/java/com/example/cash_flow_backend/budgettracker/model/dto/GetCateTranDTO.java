package com.example.cash_flow_backend.budgettracker.model.dto;


import java.math.BigDecimal;

public record GetCateTranDTO(
        int tranId,
        BigDecimal amount,
        String description,
        String date,
        int catId,
        String category
) {}
