package com.phong.mini_banking.service;

import com.phong.mini_banking.entity.BankAccount;
import com.phong.mini_banking.repository.BankAccountRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    // 1. Find bank account by ID
    public BankAccount getBankAccount(Long id) {
        return bankAccountRepository.findById(id).orElse(null);
    }

    // 2. List all bank accounts
    public List<BankAccount> getAllBankAccount() {
        return bankAccountRepository.findAll();
    }

    // 3. Add bank account
    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    // 4. Update bank account
    public BankAccount updateBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    // 5. Delete bank account
    public void deleteBankAccount(Long id) {
        bankAccountRepository.deleteById(id);
    }
}