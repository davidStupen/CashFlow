package com.example.cash_flow_backend.budgettracker.controller;

import com.example.cash_flow_backend.budgettracker.exception.CashFlowException;
import com.example.cash_flow_backend.budgettracker.model.dto.PostCategAndTranDTO;
import com.example.cash_flow_backend.budgettracker.service.CashFlowService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/cash")
public class CashFlowController {
    private CashFlowService cashFlowService;

    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
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
}
