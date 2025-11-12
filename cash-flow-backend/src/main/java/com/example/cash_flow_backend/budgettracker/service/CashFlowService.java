package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.exception.CashFlowException;
import com.example.cash_flow_backend.budgettracker.model.Category;
import com.example.cash_flow_backend.budgettracker.model.Transaction;
import com.example.cash_flow_backend.budgettracker.model.dto.CategoryDTO;
import com.example.cash_flow_backend.budgettracker.model.dto.GetCateTranDTO;
import com.example.cash_flow_backend.budgettracker.model.dto.PostCategAndTranDTO;
import com.example.cash_flow_backend.budgettracker.model.dto.TransactionDTO;
import com.example.cash_flow_backend.budgettracker.repository.CategoryRepo;
import com.example.cash_flow_backend.budgettracker.repository.TransactionRepo;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;

@Service
public class CashFlowService {private UserRepo userRepo;
    private CategoryRepo categoryRepo;
    private TransactionRepo transactionRepo;

    public CashFlowService(UserRepo userRepo, CategoryRepo categoryRepo, TransactionRepo transactionRepo) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.transactionRepo = transactionRepo;
    }
    private String formatedDate(){
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }
    public ResponseEntity<?> createCategTran(PostCategAndTranDTO postCategAndTranDTO, int idUser) throws CashFlowException, DataIntegrityViolationException {
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find"));
        List<Category> categories = user.getCategories();
        Category category = categories.stream()
                .filter(item -> item.getCategory().trim().equalsIgnoreCase(postCategAndTranDTO.category().trim())).findFirst()
                .orElse(null);
        Transaction transaction = new Transaction(postCategAndTranDTO.description(), postCategAndTranDTO.amount(), this.formatedDate());
        if (category != null){
            transaction.setCategory(category);
            transaction.setUser(user);
            this.transactionRepo.save(transaction);
        } else {
            Category category1 = new Category(postCategAndTranDTO.category());
            category1.setUser(user);
            transaction.setCategory(category1);
            this.categoryRepo.save(category1);
            transaction.setUser(user);
            this.transactionRepo.save(transaction);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findAllTranCat(int idUser) throws CashFlowException {
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find"));
        List<Category> categories = user.getCategories();
        List<GetCateTranDTO> getCateTranDTOS = categories.stream().flatMap(cat -> cat.getTransactions().stream()
                .map(tran -> new GetCateTranDTO(tran.getId(), tran.getAmount(), tran.getDate(), cat.getId(), cat.getCategory())))
                .toList();
        return new ResponseEntity<>(getCateTranDTOS, HttpStatus.OK);
    }
    public ResponseEntity<?> getCategories(int idUser) throws CashFlowException {
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find"));
        List<Category> categories = user.getCategories();
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(item -> new CategoryDTO(item.getId(), item.getCategory())).toList();
        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }
    public ResponseEntity<?> getTransactionByCategoryId(int idCategory) throws CashFlowException {
        Category category = this.categoryRepo.findById(idCategory)
                .orElseThrow(() -> new CashFlowException("Category with ID: " + idCategory + " not find."));
        List<Transaction> transactions = category.getTransactions();
        List<TransactionDTO> transactionDTOS = transactions.stream()
                .map(item -> new TransactionDTO(item.getDescription(), item.getAmount(), item.getDate(), item.getCategory().getCategory())).toList();
        return new ResponseEntity<>(transactionDTOS, HttpStatus.OK);
    }

    public ResponseEntity<String> getProfileImgIfExist(int userId) throws CashFlowException, IOException {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new CashFlowException("User with ID: " + userId + " not find"));
        if (user.getProfileImg() != null){
            String path = "data/profileImg/" + user.getProfileImg();
            byte[] bytesImg = Files.readAllBytes(new File(path).toPath());
            String data = "data:" + "image/png" + ",base64;" + Base64.getEncoder().encodeToString(bytesImg);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        return new ResponseEntity<>("No profile picture set", HttpStatus.NOT_FOUND);
    }
}
