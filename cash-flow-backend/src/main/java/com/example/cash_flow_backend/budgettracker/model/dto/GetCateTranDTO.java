package com.example.cash_flow_backend.budgettracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCateTranDTO{
        int tranId;
        BigDecimal amount;
        int catId;
        String category;

    public GetCateTranDTO(int tranId, BigDecimal amount) {
        this.tranId = tranId;
        this.amount = amount;
    }

    public GetCateTranDTO(int catId, String category) {
        this.catId = catId;
        this.category = category;
    }
}
