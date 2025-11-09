package com.example.cash_flow_backend.budgettracker.repository;

import com.example.cash_flow_backend.budgettracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
}
