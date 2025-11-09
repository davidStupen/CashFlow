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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

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
        Transaction transaction = new Transaction(postCategAndTranDTO.desc(), postCategAndTranDTO.tran());
        transaction.setUser(user);
        this.categoryRepo.save(category);
        this.transactionRepo.save(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<?> findAllTranCat(int idUser) throws CashFlowException {
        User user = this.userRepo.findById(idUser)
                .orElseThrow(() -> new CashFlowException("User with ID: " + idUser + " not find"));

    }
}
