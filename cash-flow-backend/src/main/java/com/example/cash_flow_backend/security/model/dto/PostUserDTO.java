package com.example.cash_flow_backend.security.model.dto;

import com.example.cash_flow_backend.security.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserDTO{
        String username;
        String password;
        Role role;
        String email;
        String profileImg;

}
