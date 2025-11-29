package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.exception.CashFlowException;
import com.example.cash_flow_backend.budgettracker.model.Category;
import com.example.cash_flow_backend.budgettracker.model.Transaction;
import com.example.cash_flow_backend.budgettracker.model.dto.GetCateTranDTO;
import com.example.cash_flow_backend.budgettracker.model.dto.UserDTO;
import com.example.cash_flow_backend.budgettracker.repository.CategoryRepo;
import com.example.cash_flow_backend.budgettracker.repository.TransactionRepo;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdminService {
    private UserRepo userRepo;
    private CategoryRepo categoryRepo;
    private TransactionRepo transactionRepo;
    private EmailAndPDFService emailAndPDFService;

    public AdminService(UserRepo userRepo, CategoryRepo categoryRepo, TransactionRepo transactionRepo, EmailAndPDFService emailAndPDFService) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.transactionRepo = transactionRepo;
        this.emailAndPDFService = emailAndPDFService;
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers(){
        List<User> users = this.userRepo.findAll();
        return users.stream()
                .map(item -> new UserDTO(item.getId(), item.getUsername(), item.getRole().toString().substring(5), item.getEmail())).toList();
    }

    public UserDTO deleteUserById(int userId) throws IOException {
        User user = this.userRepo.findById(userId).orElse(null);
        if (user == null){
            return null;
        }
        List<Transaction> transactions = user.getTransactions();
        List<Category> categories = user.getCategories();
        transactions.forEach(item -> this.transactionRepo.delete(item));
        categories.forEach(item -> this.categoryRepo.delete(item));
        this.userRepo.delete(user);
         if (user.getProfileImg() != null){
             Path path = Paths.get("data/profileImg/" + user.getProfileImg());
             Files.deleteIfExists(path);
         }
        return new UserDTO(user.getId(), user.getUsername(), user.getRole().toString(), user.getEmail());
    }
    public List<GetCateTranDTO> getAllData(int userId){
        User user = this.userRepo.findById(userId).orElse(null);
        if (user == null){
            return null;
        }
        List<Category> categories = user.getCategories();
        return categories.stream()
                .flatMap(cat -> cat.getTransactions().stream()
                        .map(tran -> new GetCateTranDTO(tran.getId(), tran.getAmount(), tran.getDescription(), tran.getDate(), cat.getId(), cat.getCategory())))
                .toList();
    }

    public void sendEmailToUser(int idUser, String input) throws Exception {
        if (idUser == -1){
            throw new CashFlowException("The error occurred on the frontend with id" + idUser);
        }
        User user = this.userRepo.findById(idUser).orElseThrow(() -> new CashFlowException("user is no find"));
        this.emailAndPDFService
                .simpleSentEmail(user.getEmail(), "Company name", input);
    }
    public List<UserDTO> searchByUsername(String username) throws CashFlowException {
        List<User> users = this.userRepo.findByUsernameContaining(username)
                .orElseThrow(() -> new CashFlowException("user not exists"));
        return users.stream()
                .map(item -> new UserDTO(item.getId(), item.getUsername(), item.getRole().toString().substring(5), item.getEmail())).toList();
    }
}
