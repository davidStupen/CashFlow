package com.example.cash_flow_backend.budgettracker.model;

import com.example.cash_flow_backend.security.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal tran;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
