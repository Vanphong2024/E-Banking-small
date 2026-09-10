package com.phong.mini_banking.entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
 import jakarta.persistence.Id;
 import jakarta.persistence.JoinColumn;
 import jakarta.persistence.ManyToOne;

    
  @Entity
@Table(name = "transactions")
public class transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

     @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount bankAccount;
    
       @Column(name = "transaction_code", nullable = false, unique = true, length = 50)
    private String transactionCode;
      
    @Column(name = "transaction_type", nullable = false, length = 30)
    private String transactionType;
     
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
        @Column(name = "balance_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
