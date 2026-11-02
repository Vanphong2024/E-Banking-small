package com.phong.mini_banking.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table 
public class Admin {
    @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.ADMIN;

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @Column(name = "can_manage_customer_accounts", nullable = false)
    private boolean canManageCustomerAccounts = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public Long getAdminId() {
        return adminId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCanManageCustomerAccounts() {
        return canManageCustomerAccounts;
    }

    public void setCanManageCustomerAccounts(boolean canManageCustomerAccounts) {
        this.canManageCustomerAccounts = canManageCustomerAccounts;
    }
}
