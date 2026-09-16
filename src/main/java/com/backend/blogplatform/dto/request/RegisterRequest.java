package com.backend.blogplatform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username must be required!")
    private String username;

    @NotBlank(message = "Email must be required!")
    @Email(message = "Please enter valid email!")
    private String email;

    @NotBlank(message = "Password must be required!")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
