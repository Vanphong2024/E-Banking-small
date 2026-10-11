package com.phong.mini_banking.service;

import com.phong.mini_banking.entity.*;
import com.phong.mini_banking.repository.TransactionRepository;
import com.phong.mini_banking.repository.BankAccountRepository;
import com.phong.mini_banking.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;

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
@Transactional
public void transferMoney(User sender, String receiverAccountNumber, BigDecimal amount) {
    // lấy thông tin người gửi từ userId 
    if(amount==null || amount.stripTrailingZeros().scale() > 0){
        throw new IllegalArgumentException("Số tiền chuyển phải lớn hơn 0");
    }
// tài khoản người gửi A 
  BankAccount senderAccount = bankAcccountRepository.findByUser(sender)
        .orElseThrow(() ->
            new IllegalArgumentException("Không tìm thấy tài khoản A")
        );
        if (amount== null || amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("số tiền phải lớn hơn 0 ");
        }
// tài khoàn của người b 
BankAccount receiverAccount = bankAcccountRepository
        .findByAccountNumber(receiverAccountNumber)
        .orElseThrow(() ->
            new IllegalArgumentException("Không tìm thấy tài khoản B")
        );
 
        if (senderAccount.getAccountId().equals(receiverAccount.getAccountId())) {
            throw new IllegalArgumentException("Không thể chuyển tiền cho chính mình");
        }
// kiểm tra số dư của người gủi 
if (senderAccount.getBalance().compareTo(amount) < 0) {
    throw new IllegalArgumentException("Số dư tài khoản không đủ để thực hiện giao dịch");
}
// từ tài khoản a 
BigDecimal senderNewBalance = senderAccount.getBalance().subtract(amount);
//cộng tiền vào tài khoản b
BigDecimal receiverNewBalance = receiverAccount.getBalance().add(amount);
// Cập nhật số dư A
senderAccount.setBalance(senderNewBalance);

// Cập nhật số dư B
receiverAccount.setBalance(receiverNewBalance);
}


}
