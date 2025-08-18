package com.applab.library_management.service;

import com.applab.library_management.dto.LoginRequestDTO;
import com.applab.library_management.dto.RegistrationRequestDTO;
import com.applab.library_management.exception.UserExceptions;
import com.applab.library_management.model.User;
import com.applab.library_management.model.UserRole;
import com.applab.library_management.repository.UserRepository;
import com.applab.library_management.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(RegistrationRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Registration failed - Email already exists: {}", request.getEmail());
            throw new UserExceptions.EmailAlreadyExistsException("Email already exists");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("Registration failed - Username already exists: {}", request.getUsername());
            throw new UserExceptions.UsernameAlreadyExistsException("Username already exists");
        }

        //Encrypt the password
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(request.getRole() != null ? UserRole.valueOf(request.getRole().toUpperCase()) : UserRole.MEMBER);

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {} (ID: {})", savedUser.getEmail(), savedUser.getId());
        return savedUser;
    }

    public String authenticateAndGenerateToken(LoginRequestDTO loginRequest){        
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Authentication failed - User not found: {}", loginRequest.getEmail());
                    return new UserExceptions.UserNotFoundException("User not found");
                });

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            logger.warn("Authentication failed - Invalid credentials for user: {}", loginRequest.getEmail());
            throw new UserExceptions.InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        logger.info("Authentication successful for user: {} (Role: {})", user.getEmail(), user.getRole());
        return token;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User not found by email: {}", email);
                    return new UserExceptions.UserNotFoundException("User not found");
                });
    }
}
