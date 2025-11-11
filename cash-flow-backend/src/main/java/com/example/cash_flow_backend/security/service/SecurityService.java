package com.example.cash_flow_backend.security.service;

import com.example.cash_flow_backend.budgettracker.service.EmailAndPDFService;
import com.example.cash_flow_backend.security.exeption.UserException;
import com.example.cash_flow_backend.security.model.Role;
import com.example.cash_flow_backend.security.model.User;
import com.example.cash_flow_backend.security.model.dto.PostUserDTO;
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
    private EmailAndPDFService emailAndPDFService;

    public SecurityService(AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder, JwtService jwtService, UserRepo userRepo, EmailAndPDFService emailAndPDFService) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.emailAndPDFService = emailAndPDFService;
    }

    private void wrongRequestException(PostUserDTO user) throws UserException {
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
    public ResponseEntity<?> saveUser(PostUserDTO userDTO, MultipartFile img) throws Exception {
        this.wrongRequestException(userDTO);
        userDTO.setRole(Role.ROLE_USER);
        userDTO.setPassword(this.encoder.encode(userDTO.getPassword()));
        if (img != null){
            String storageName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));
            userDTO.setProfileImg(storageName);
            User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getRole(), userDTO.getEmail(), userDTO.getProfileImg());
            this.userRepo.save(user);
            String dirPath = "data/profileImg";
            if (Files.notExists(Paths.get(dirPath))){
                Files.createDirectories(Paths.get(dirPath));
            }
            Files.copy(img.getInputStream(), Paths.get(dirPath).resolve(storageName), StandardCopyOption.REPLACE_EXISTING);
        } else {
            User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getRole(), userDTO.getEmail(), userDTO.getProfileImg());
            this.userRepo.save(user);
        }
        this.emailAndPDFService.simpleSentEmail(userDTO.getEmail(), "Welcome", "Thank you for registering " + userDTO.getUsername());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(PostUserDTO postUserDTO) throws UsernameNotFoundException {
        this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(postUserDTO.getUsername(), postUserDTO.getPassword()));
        Optional<User> findByUsername = this.userRepo.findByUsername(postUserDTO.getUsername());
        postUserDTO.setRole(findByUsername.get().getRole());
        String token = this.jwtService.generateToken(new UserTokenDTO(postUserDTO.getUsername(), postUserDTO.getRole(), findByUsername.get().getId()));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
