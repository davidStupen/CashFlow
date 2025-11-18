package com.example.cash_flow_backend.budgettracker.controller;

import com.example.cash_flow_backend.budgettracker.exception.CashFlowException;
import com.example.cash_flow_backend.budgettracker.model.dto.PostCategAndTranDTO;
import com.example.cash_flow_backend.budgettracker.service.ApiService;
import com.example.cash_flow_backend.budgettracker.service.CashFlowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/cash")
public class CashFlowController {
    private CashFlowService cashFlowService;
    private ApiService apiService;

    public CashFlowController(CashFlowService cashFlowService, ApiService apiService) {
        this.cashFlowService = cashFlowService;
        this.apiService = apiService;
    }

    @PostMapping("/create/{idUser}")
    public ResponseEntity<?> addNewItem(@RequestBody PostCategAndTranDTO postCategAndTranDTO, @PathVariable int idUser){
        try {
            return this.cashFlowService.createCategTran(postCategAndTranDTO, idUser);
        } catch (CashFlowException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/items/{idUser}")
    public ResponseEntity<?> getAllItem(@PathVariable int idUser, @RequestParam(required = false, defaultValue = "false", name = "pdf") boolean isPDF, HttpServletResponse response) {
        try {
            return this.cashFlowService.findAllTranCat(idUser, isPDF, response);
        } catch (CashFlowException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/rate")
    public ResponseEntity<?> getRate(){
        try {
            return this.apiService.apiEurCzk();
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("An error that occurs when Jackson fails to parse JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/categories/{idUser}")
    public ResponseEntity<?> getCategories(@PathVariable int idUser){
        try {
            return this.cashFlowService.getCategories(idUser);
        } catch (CashFlowException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/transactions-by-category/{idCategory}")
    public ResponseEntity<?> getTransactionsByCategoryId(@PathVariable int idCategory){
        try {
            return this.cashFlowService.getTransactionByCategoryId(idCategory);
        } catch (CashFlowException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/profile-img/{userId}")
    public ResponseEntity<String> getProfileImg(@PathVariable int userId){
        try {
            return this.cashFlowService.getProfileImgIfExist(userId);
        } catch (CashFlowException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/expenses/{userId}")
    public ResponseEntity<?> getExpenses(@PathVariable int userId, @RequestParam int idC){
        try {
            return this.cashFlowService.totalExpenses(userId, idC);
        } catch (CashFlowException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/chart/{idUser}")
    public ResponseEntity<?> getDataForChart(@PathVariable int idUser) {
        try {
            return this.cashFlowService.getDataForChart(idUser);
        } catch (CashFlowException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
