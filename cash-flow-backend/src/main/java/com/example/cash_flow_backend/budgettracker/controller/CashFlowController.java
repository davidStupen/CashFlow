package com.example.cash_flow_backend.budgettracker.controller;

import com.example.cash_flow_backend.budgettracker.service.CashFlowService;
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
    @PostMapping("/create")
    public ResponseEntity<?> addNewItem(@RequestBody PostCategAndTranDTO postCategAndTranDTO, @PathVariable int idUser){
        return this.cashFlowService.createCategTran(postCategAndTranDTO, idUser);
    }
}
