package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.model.dto.UserDTO;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Service
public class AdminService {
    private UserRepo userRepo;

    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @GetMapping("/users")
    public List<UserDTO> getUsers(){
        List<User> users = this.userRepo.findAll();
        return users.stream()
                .map(item -> new UserDTO(item.getId(), item.getUsername(), item.getRole().toString(), item.getEmail())).toList();
    }
}
