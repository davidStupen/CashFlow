package com.example.cash_flow_backend.budgettracker.model.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EuroDTO(
        double dev_stred
) {
}
