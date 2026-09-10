package com.phong.mini_banking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "account_id")
      private Long accountId;

@OneToOne 
   @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; 
   

    @Column(name="account_number", nullable=false , unique=true , length = 20 )
private String accountNumber; 
 
@Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;


    @Column(name = "account_type", length = 30)
    private String accountType = "SAVINGS";


    @Column(length = 20)
    private String status = "ACTIVE";

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
