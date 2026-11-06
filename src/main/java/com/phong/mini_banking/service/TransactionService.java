package com.phong.mini_banking.service;

import com.phong.mini_banking.entity.*;
import com.phong.mini_banking.repository.TransactionRepository;
import com.phong.mini_banking.repository.BankAccountRepository;
import com.phong.mini_banking.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAcccountRepository;
    private final UserRepository userRepository;

    // Constructor
    public TransactionService(
            TransactionRepository transactionRepository,
            BankAccountRepository bankAcccountRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.bankAcccountRepository = bankAcccountRepository;
        this.userRepository = userRepository;
    }
// chức năng chuyển tiền 
@Transactional(rollbackFor = Exception.class)
public void transferMoney(User sender, String receiverAccountNumber, BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0
            || amount.stripTrailingZeros().scale() > 0
            || amount.compareTo(new BigDecimal("9999999999999.99")) > 0) {
        throw new IllegalArgumentException("Số tiền chuyển phải lớn hơn 0");
    }

    BankAccount senderReference = bankAcccountRepository.findByUser(sender)
        .orElseThrow(() ->
            new IllegalArgumentException("Không tìm thấy tài khoản A")
        );

    BankAccount receiverReference = bankAcccountRepository
        .findByAccountNumber(receiverAccountNumber)
        .orElseThrow(() ->
            new IllegalArgumentException("Không tìm thấy tài khoản B")
        );
 
        if (senderReference.getAccountId().equals(receiverReference.getAccountId())) {
            throw new IllegalArgumentException("Không thể chuyển tiền cho chính mình");
        }
    Long firstId = Math.min(senderReference.getAccountId(), receiverReference.getAccountId());
    Long secondId = Math.max(senderReference.getAccountId(), receiverReference.getAccountId());
    BankAccount first = bankAcccountRepository.findLockedById(firstId).orElseThrow();
    BankAccount second = bankAcccountRepository.findLockedById(secondId).orElseThrow();
    BankAccount senderAccount = senderReference.getAccountId().equals(firstId) ? first : second;
    BankAccount receiverAccount = receiverReference.getAccountId().equals(firstId) ? first : second;
    if (!"ACTIVE".equals(senderAccount.getStatus()) || !"ACTIVE".equals(receiverAccount.getStatus())) {
        throw new IllegalArgumentException("Tài khoản gửi hoặc nhận đang bị khóa, không thể giao dịch.");
    }
    if (receiverAccount.getBalance().add(amount).compareTo(new BigDecimal("9999999999999.99")) > 0) {
        throw new IllegalArgumentException("Số dư tài khoản nhận vượt hạn mức.");
    }
    if (senderAccount.getBalance().compareTo(amount) < 0) {
        throw new IllegalArgumentException("Số dư tài khoản không đủ để thực hiện giao dịch");
    }

    senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
    receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

    bankAcccountRepository.save(senderAccount);
    bankAcccountRepository.save(receiverAccount);

    transaction transaction = new transaction();
    transaction.setAmount(amount);
    transaction.setSenderAccount(senderAccount);
    transaction.setReceiverAccount(receiverAccount);
    transaction.setCreatedAt(LocalDateTime.now());
    transactionRepository.save(transaction);

}

public transaction getTransactionById(Long id) {
    return transactionRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException("Không tìm thấy giao dịch")
            );
}
// Lấy danh sách giao dịch của người dùng  
public List<transaction> getTransactionHistory(Long accountId) {
    return transactionRepository
            .findBySenderAccount_AccountIdOrReceiverAccount_AccountIdOrderByCreatedAtDesc(
                    accountId,
                    accountId);
}

}
