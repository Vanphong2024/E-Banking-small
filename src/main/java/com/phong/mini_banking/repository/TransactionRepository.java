package com.phong.mini_banking.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.phong.mini_banking.entity.transaction;

public interface TransactionRepository extends JpaRepository<transaction,Long> {
    
}
