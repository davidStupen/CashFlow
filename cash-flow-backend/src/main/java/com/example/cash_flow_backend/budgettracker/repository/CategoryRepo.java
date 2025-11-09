package com.example.cash_flow_backend.budgettracker.repository;

import com.example.cash_flow_backend.budgettracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategory(String category);
}
