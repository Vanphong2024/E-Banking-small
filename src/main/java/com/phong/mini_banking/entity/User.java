package com.phong.mini_banking.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
    import jakarta.persistence.Id;
    import jakarta.persistence.PrePersist;
@Entity
@Table(name="user")
public class User {
 @Id 
 @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long userId;
   
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;

     @Column(name = "full_name", nullable = false)
    private String fullName;

        @Column(name = "email", unique = true)
    private String email;

     @Column(name = "created_at")
    private LocalDateTime createdAt;


public void setUserId(Long userId) {
    this.userId = userId;
}
public Long getUserId(){
    return userId ; 
}
    // Getter / Setter cho username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    // Getter / Setter cho password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // Getter / Setter cho fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    // Getter / Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // Getter / Setter cho createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    // Tự động tạo thời gian khi User mới được tạo
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

