package com.example.cash_flow_backend.budgettracker.model.dto;

import java.math.BigDecimal;

public record PostCategAndTranDTO(
        String category,
        BigDecimal tran
) {
}
