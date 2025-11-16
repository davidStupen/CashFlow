package com.example.cash_flow_backend.budgettracker.model.dto;

import java.math.BigDecimal;

public record TransactionChartDTO(
        String date,
        BigDecimal amount
) {
}
