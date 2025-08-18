package com.applab.library_management.controller;

import com.applab.library_management.dto.ApiResponse;
import com.applab.library_management.dto.LoginRequestDTO;
import com.applab.library_management.dto.LoginResponseDTO;
import com.applab.library_management.dto.RegistrationRequestDTO;
import com.applab.library_management.model.User;
import com.applab.library_management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegistrationRequestDTO request) {
        User user = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login (@Valid @RequestBody LoginRequestDTO loginRequest) {
        String token = authService.authenticateAndGenerateToken(loginRequest);
        User user = authService.getUserByEmail(loginRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Login successful", new LoginResponseDTO(token, user)));
    }
}
