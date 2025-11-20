package com.example.demo.dto;

import com.example.demo.entity.User;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UserResponse user;

    public AuthResponse() {
    }

    public AuthResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public static class UserResponse {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private User.Role role;

        public UserResponse() {
        }

        public UserResponse(Long id, String email, String firstName, String lastName, User.Role role) {
            this.id = id;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
        }

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public User.Role getRole() {
            return role;
        }

        public void setRole(User.Role role) {
            this.role = role;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}