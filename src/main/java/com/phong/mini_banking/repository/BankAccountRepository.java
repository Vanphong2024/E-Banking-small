package com.phong.mini_banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phong.mini_banking.entity.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {

    
}
    

