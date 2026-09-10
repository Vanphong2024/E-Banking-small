package com.phong.mini_banking.service;

import com.phong.mini_banking.entity.*;
import com.phong.mini_banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // Constructor
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // 1. Tìm transaction theo ID
    public transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    // 2. Lấy tất cả transaction
    public List<transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // 3. Thêm transaction
    public transaction saveTransaction(transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // 4. Cập nhật transaction
    public transaction updateTransaction(transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // 5. Xóa transaction theo ID
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}