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
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // senders and recives 
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private BankAccount senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    
    private BankAccount receiverAccount;

}   
