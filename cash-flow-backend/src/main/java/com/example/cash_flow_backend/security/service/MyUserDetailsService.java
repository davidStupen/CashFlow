package com.example.cash_flow_backend.security.service;

import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.model.UserPrincipal;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {
    private UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not find"));
        return new UserPrincipal(user);
    }
}
