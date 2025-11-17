package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.model.Category;
import com.example.cash_flow_backend.budgettracker.model.Transaction;
import com.example.cash_flow_backend.budgettracker.model.dto.UserDTO;
import com.example.cash_flow_backend.budgettracker.repository.CategoryRepo;
import com.example.cash_flow_backend.budgettracker.repository.TransactionRepo;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Service
public class AdminService {
    private UserRepo userRepo;
    private CategoryRepo categoryRepo;
    private TransactionRepo transactionRepo;

    public AdminService(UserRepo userRepo, CategoryRepo categoryRepo, TransactionRepo transactionRepo) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.transactionRepo = transactionRepo;
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers(){
        List<User> users = this.userRepo.findAll();
        return users.stream()
                .map(item -> new UserDTO(item.getId(), item.getUsername(), item.getRole().toString().substring(5), item.getEmail())).toList();
    }

    public UserDTO deleteUserById(int userId) {
        User user = this.userRepo.findById(userId).orElse(null);
        if (user == null){
            return null;
        }
        List<Transaction> transactions = user.getTransactions();
        List<Category> categories = user.getCategories();
        transactions.forEach(item -> this.transactionRepo.delete(item));
        categories.forEach(item -> this.categoryRepo.delete(item));
        this.userRepo.delete(user);
        return new UserDTO(user.getId(), user.getUsername(), user.getRole().toString(), user.getEmail());
    }
}
