package com.example.cash_flow_backend.budgettracker.model.dto;


import java.math.BigDecimal;
import java.util.Date;

public record GetCateTranDTO(
        int tranId,
        BigDecimal amount,
        Date date,
        int catId,
        String category
) {}
