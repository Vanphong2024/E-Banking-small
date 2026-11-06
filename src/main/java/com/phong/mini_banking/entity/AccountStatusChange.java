package com.phong.mini_banking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_status_changes")
public class AccountStatusChange {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;
    @Column(nullable = false)
    private String previousStatus;
    @Column(nullable = false)
    private String newStatus;
    @Column(nullable = false)
    private String performedBy;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected AccountStatusChange() {}
    public AccountStatusChange(BankAccount account, String previousStatus, String newStatus, String performedBy) {
        this.account = account;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.performedBy = performedBy;
        this.createdAt = LocalDateTime.now();
    }
    public String getPreviousStatus() { return previousStatus; }
    public String getNewStatus() { return newStatus; }
    public String getPerformedBy() { return performedBy; }
}
