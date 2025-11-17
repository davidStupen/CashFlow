package com.example.cash_flow_backend.budgettracker.controller;

import com.example.cash_flow_backend.budgettracker.model.dto.UserDTO;
import com.example.cash_flow_backend.budgettracker.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        List<UserDTO> userDTOS = this.adminService.getUsers();
        if ( ! userDTOS.isEmpty()){
            return new ResponseEntity<>(userDTOS, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestParam int userId) {
        UserDTO userDTO;
        try {
            userDTO = this.adminService.deleteUserById(userId);
        } catch (IOException e) {
            return new ResponseEntity<>("img is not delete", HttpStatus.NOT_ACCEPTABLE);
        }
        if (userDTO == null){
            return new ResponseEntity<>("user not find", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
