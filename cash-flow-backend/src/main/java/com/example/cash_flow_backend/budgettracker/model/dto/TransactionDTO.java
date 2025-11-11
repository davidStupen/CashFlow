package com.example.cash_flow_backend.budgettracker.model.dto;

import java.math.BigDecimal;

public record TransactionDTO(
    String description,
    BigDecimal tran,
    String date
) {
}
