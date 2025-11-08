package com.example.cash_flow_backend.security.service;

import com.example.cash_flow_backend.security.exeption.UserException;
import com.example.cash_flow_backend.security.model.Role;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.model.dto.UserTokenDTO;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
public class SecurityService {
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder encoder;
    private JwtService jwtService;
    private UserRepo userRepo;

    public SecurityService(AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder, JwtService jwtService, UserRepo userRepo) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }
    private void wrongRequestException(User user) throws UserException {
        if (user.getUsername().isEmpty()){
            throw new UserException("Username is required");
        }
        if (user.getEmail().isEmpty()){
            throw new UserException("Email is required");
        }
        if (user.getPassword().isEmpty()){
            throw new UserException("Password is required");
        }
        if ( ! user.getEmail().contains("@")){
            throw new UserException("The email has to containing @");
        }
    }
    public ResponseEntity<?> saveUser(User user, MultipartFile img) throws UserException, IOException, DataIntegrityViolationException {
        this.wrongRequestException(user);
        user.setPassword(this.encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        if (img != null){
            String storageName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));
            user.setProfileImg(storageName);
            String dirPath = "data/profileImg";
            if (Files.notExists(Paths.get(dirPath))){
                Files.createDirectories(Paths.get(dirPath));
            }
            Files.copy(img.getInputStream(), Paths.get(dirPath).resolve(storageName), StandardCopyOption.REPLACE_EXISTING);
        }
        this.userRepo.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(User user) throws UsernameNotFoundException {
        this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        Optional<User> findByUsername = this.userRepo.findByUsername(user.getUsername());
        user.setRole(findByUsername.get().getRole());
        String token = this.jwtService.generateToken(new UserTokenDTO(user.getUsername(), user.getRole()));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
