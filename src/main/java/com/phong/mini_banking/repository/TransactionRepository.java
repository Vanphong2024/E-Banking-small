package com.phong.mini_banking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phong.mini_banking.entity.transaction;

public interface TransactionRepository extends JpaRepository<transaction,Long> {
     List<transaction> findBySenderAccount_AccountId(Long accountId);

    List<transaction> findByReceiverAccount_AccountId(Long accountId);
}
