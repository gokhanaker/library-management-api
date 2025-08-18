package com.applab.library_management.dto;

import com.applab.library_management.model.User;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private User user;
    
    public LoginResponseDTO(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
