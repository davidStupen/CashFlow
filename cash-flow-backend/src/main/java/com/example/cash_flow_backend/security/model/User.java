package com.example.cash_flow_backend.security.model;

import com.example.cash_flow_backend.budgettracker.model.Category;
import com.example.cash_flow_backend.budgettracker.model.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, length = 25)
    private String username;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false, unique = true, length = 55)
    private String email;
    @Column(unique = true)
    private String profileImg;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(nullable = false, length = 55)
    private List<Transaction> transactions;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(nullable = false, length = 55)
    private List<Category> categories;

    public User(String username, String password, Role role, String email, String profileImg) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.profileImg = profileImg;
    }
}
