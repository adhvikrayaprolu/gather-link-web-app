package com.gather_link.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames="username"),@UniqueConstraint(columnNames="email")})
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "username", length = 50, nullable = false)
    private String username;
    
    @Column(name = "email", length = 100, nullable = false)
    private String email;
    
    @Column(name = "password", length = 100, nullable = false)
    private String password;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getUserId() {
    	return userId;
    }

    public void setUserId(Long user_id) {
    	this.userId = user_id;
    }

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
    
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getPassword() {
    	return password;
    }

    public void setPassword(String password) {
    	this.password = password;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}