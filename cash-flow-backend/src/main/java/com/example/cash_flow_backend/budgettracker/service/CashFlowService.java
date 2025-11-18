package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.exception.CashFlowException;
import com.example.cash_flow_backend.budgettracker.model.Category;
import com.example.cash_flow_backend.budgettracker.model.Transaction;
import com.example.cash_flow_backend.budgettracker.model.dto.*;
import com.example.cash_flow_backend.budgettracker.repository.CategoryRepo;
import com.example.cash_flow_backend.budgettracker.repository.TransactionRepo;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CashFlowService {
    private UserRepo userRepo;
    private CategoryRepo categoryRepo;
    private TransactionRepo transactionRepo;
    private EmailAndPDFService emailAndPDFService;

    public CashFlowService(UserRepo userRepo, CategoryRepo categoryRepo, TransactionRepo transactionRepo, EmailAndPDFService emailAndPDFService) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.transactionRepo = transactionRepo;
        this.emailAndPDFService = emailAndPDFService;
    }

    private String formatedDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }
    private void checkNewCatTran(PostCategAndTranDTO newCatPost) throws CashFlowException {
        if (newCatPost.amount().doubleValue() <= 0){
            throw new CashFlowException("The price must be positive.");
        }
        if (newCatPost.category().isEmpty() || newCatPost.description().isEmpty()){
            throw new CashFlowException("The category or description is required.");
        }
    }
    public ResponseEntity<?> createCategTran(PostCategAndTranDTO postCategAndTranDTO, int idUser) throws CashFlowException, DataIntegrityViolationException {
        this.checkNewCatTran(postCategAndTranDTO);
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find."));
        List<Category> categories = user.getCategories();
        Category category = categories.stream()
                .filter(item -> item.getCategory().trim().equalsIgnoreCase(postCategAndTranDTO.category().trim())).findFirst()
                .orElse(null);
        Transaction transaction = new Transaction(postCategAndTranDTO.description(), postCategAndTranDTO.amount(), this.formatedDate(LocalDate.now()));
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

    public ResponseEntity<?> findAllTranCat(int idUser, boolean isPDF, HttpServletResponse response) throws CashFlowException, IOException {
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find"));
        List<Category> categories = user.getCategories();
        List<GetCateTranDTO> getCateTranDTOS = categories.stream().flatMap(cat -> cat.getTransactions().stream()
                .map(tran -> new GetCateTranDTO(tran.getId(), tran.getAmount(), tran.getDescription(), tran.getDate(), cat.getId(), cat.getCategory())))
                .toList();
        if (isPDF){
            this.emailAndPDFService.pdfDocument(response, getCateTranDTOS);
            return new ResponseEntity<>("pdf created", HttpStatus.OK);
        }
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
            String data = "data:" + "image/png" + ";base64," + Base64.getEncoder().encodeToString(bytesImg);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        return new ResponseEntity<>("No profile picture set", HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<?> totalExpenses(int userId, int idCategory) throws CashFlowException {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new CashFlowException("User with ID: " + userId + " not find"));
        List<Transaction> transactions = user.getTransactions();
        if (transactions.isEmpty()){
            return new ResponseEntity<>("no transactions yet", HttpStatus.NO_CONTENT);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<BigDecimal> prices = new ArrayList<>();
        if (idCategory == -1){
            transactions.stream()
                    .filter(item -> LocalDate.parse(item.getDate(), formatter).isAfter(LocalDate.now().minusDays(28)))
                    .map(item -> item.getAmount())
                    .forEach(item -> prices.add(item));
        } else {
            transactions.clear();
            Category category = this.categoryRepo.findById(idCategory).orElseThrow(() -> new CashFlowException("id category is not find " + idCategory));
            transactions.addAll(category.getTransactions());
            transactions.stream()
                    .filter(item -> LocalDate.parse(item.getDate(), formatter).isAfter(LocalDate.now().minusDays(28)))
                    .map(item -> item.getAmount())
                    .forEach(item -> prices.add(item));
        }
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal pri : prices){
            result = result.add(pri);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    public ResponseEntity<?> getDataForChart(int userId) throws CashFlowException {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new CashFlowException("User with ID: " + userId + " not find"));
        List<Transaction> transactions = user.getTransactions();
        if (transactions.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<TransactionChartDTO> chartDTOS = transactions.stream()
                .map(item -> new TransactionChartDTO(item.getDate(), item.getAmount())).toList();
        Map<String, BigDecimal> values = new HashMap<>();
        String data;
        BigDecimal amount = BigDecimal.ZERO;
        for (int i = 0; i < chartDTOS.size(); i++) {
            data = chartDTOS.get(i).date();
            for (int j = 0; j < chartDTOS.size(); j++) {
                if (data.equals(chartDTOS.get(j).date())){
                    amount = amount.add(chartDTOS.get(j).amount());
                }
            }
            values.put(data, amount);
            amount = BigDecimal.ZERO;
        }
        List<TransactionChartDTO> chartDTOS1 = values.entrySet().stream()
                .map(item ->new TransactionChartDTO(item.getKey(), item.getValue())).toList();
        return new ResponseEntity<>(chartDTOS1, HttpStatus.OK);
    }
}
