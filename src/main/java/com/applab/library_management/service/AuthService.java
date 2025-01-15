package com.applab.library_management.service;

import com.applab.library_management.dto.LoginRequestDTO;
import com.applab.library_management.dto.RegistrationRequestDTO;
import com.applab.library_management.exception.UserExceptions;
import com.applab.library_management.model.User;
import com.applab.library_management.repository.UserRepository;
import com.applab.library_management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(RegistrationRequestDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserExceptions.EmailAlreadyExistsException("Email already exists");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserExceptions.UsernameAlreadyExistsException("Username already exists");
        }

        //Encrypt the password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(request.getRole() != null ? request.getRole() : "MEMBER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public String authenticateAndGenerateToken(LoginRequestDTO loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new UserExceptions.InvalidCredentialsException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
