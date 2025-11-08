package com.example.cash_flow_backend.security.service;

import com.example.cash_flow_backend.security.UserException;
import com.example.cash_flow_backend.security.model.Role;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private boolean isExistUserInDB(User user) throws UserException {
        Optional<User> checkUsername = this.userRepo.findByUsername(user.getUsername());
        Optional<User> checkEmail = this.userRepo.findByEmail(user.getEmail());
        Optional<User> checkImgName = this.userRepo.findByProfileImg(user.getProfileImg());
        if (checkUsername.isPresent()){
            throw new UserException("The username already exists " + user.getUsername());
        }
        if (checkEmail.isPresent()) {
            throw new UserException("The email already exist " + user.getEmail());
        }
        if (checkImgName.isPresent()) {
            throw new UserException("The profile img name already exist " + user.getProfileImg());
        }
        return false;
    }
    public ResponseEntity<?> saveUser(User user, MultipartFile img) throws UserException, IOException {
        if ( ! this.isExistUserInDB(user)){
            user.setPassword(this.encoder.encode(user.getPassword()));
            user.setRole(Role.ROLE_USER);
            String dirPath = "data/profileImg";
            String storageName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));
            if (Files.notExists(Paths.get(dirPath))){
                Files.createDirectories(Paths.get(dirPath));
            }
            Files.copy(img.getInputStream(), Paths.get(dirPath).resolve(storageName), StandardCopyOption.REPLACE_EXISTING);
            this.userRepo.save(user);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(User user) {
    }
}
