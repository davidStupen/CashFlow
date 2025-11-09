package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.exception.CashFlowException;
import com.example.cash_flow_backend.budgettracker.model.Category;
import com.example.cash_flow_backend.budgettracker.model.Transaction;
import com.example.cash_flow_backend.budgettracker.model.dto.GetCateTranDTO;
import com.example.cash_flow_backend.budgettracker.model.dto.PostCategAndTranDTO;
import com.example.cash_flow_backend.budgettracker.repository.CategoryRepo;
import com.example.cash_flow_backend.budgettracker.repository.TransactionRepo;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashFlowService {private UserRepo userRepo;
    private CategoryRepo categoryRepo;
    private TransactionRepo transactionRepo;

    public CashFlowService(UserRepo userRepo, CategoryRepo categoryRepo, TransactionRepo transactionRepo) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.transactionRepo = transactionRepo;
    }

    public ResponseEntity<?> createCategTran(PostCategAndTranDTO postCategAndTranDTO, int idUser) throws CashFlowException, DataIntegrityViolationException {
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find"));
        Category category = new Category(postCategAndTranDTO.category());
        category.setUser(user);
        Transaction transaction = new Transaction(postCategAndTranDTO.description(), postCategAndTranDTO.tran());
        transaction.setUser(user);
        transaction.setCategory(category);
        this.categoryRepo.save(category);
        this.transactionRepo.save(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findAllTranCat(int idUser) throws CashFlowException {
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find"));
        List<Category> categories = user.getCategories();
        List<GetCateTranDTO> getCateTranDTOS = categories.stream().flatMap(cat -> cat.getTransactions().stream()
                .map(tran -> new GetCateTranDTO(tran.getId(), tran.getTran(), cat.getId(), cat.getCategory())))
                .toList();
        return new ResponseEntity<>(getCateTranDTOS, HttpStatus.OK);
    }
}
