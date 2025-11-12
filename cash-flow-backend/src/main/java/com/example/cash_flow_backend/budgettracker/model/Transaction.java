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
    @Column(nullable = false, length = 55)
    private String description;
    private BigDecimal amount;
    @Column(nullable = false, length = 13)
    private String date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Transaction(String description, BigDecimal amount, String date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }
}
