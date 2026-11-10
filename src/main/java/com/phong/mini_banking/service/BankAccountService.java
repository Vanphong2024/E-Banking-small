package com.phong.mini_banking.service;
import java.util.concurrent.ThreadLocalRandom;
import com.phong.mini_banking.entity.BankAccount;
import com.phong.mini_banking.repository.BankAccountRepository;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
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

    // // 4. Update bank account
    // public BankAccount updateBankAccount(BankAccount bankAccount) {
    //     return bankAccountRepository.save(bankAccount);
    // }

    // // 5. Delete bank account
    // public void deleteBankAccount(Long id) {
    //     bankAccountRepository.deleteById(id);
    // }
    // 6. sinh số tài khoản 
public String gererateAccountNumber(){
    String accountNumber;
    do {
       accountNumber = String.valueOf(
        ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L));
       
    } while (bankAccountRepository.existsByAccountNumber(accountNumber));
    return accountNumber;
}
@Transactional
public BankAccount openAccount(User user){

    if (user.getRole() != null && user.getRole() != Role.CUSTOMER) {
        throw new IllegalArgumentException("Chỉ được mở tài khoản cho khách hàng.");
    }
    if (user.getRole() == null) {
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
    }

    var existingAccount = bankAccountRepository.findByUser(user);
    if (existingAccount.isPresent()) {
        return existingAccount.get();
    }
    String accountNumber = gererateAccountNumber();
BankAccount bankAccount = new BankAccount();
bankAccount.setAccountNumber(accountNumber);
bankAccount.setUser(user);

//Đồng bồ quan hệ giữa User và BankAccount
user.setBankAccount(bankAccount);
return saveBankAccount(bankAccount);


}
// viết hàm lấy id người dùng từ userId
public Long getReciverName(String accountNumber){
    // Lấy thông tin tài khoản từ số tài khoản
    BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() ->
                new IllegalArgumentException("Không tìm thấy tài khoản")
            );
    return account.getUser().getUserId();
}

// Tìm tên người nhận theo số tài khoản
public String getReceiverName(String accountNumber) {
    if (accountNumber == null || accountNumber.isBlank()) {
        throw new IllegalArgumentException("Số tài khoản người nhận không hợp lệ");
    }

    BankAccount account = bankAccountRepository
            .findByAccountNumber(accountNumber.trim())
            .orElseThrow(() ->
                    new IllegalArgumentException("Không tìm thấy tài khoản"));

    if (account.getUser() == null || account.getUser().getFullName() == null
            || account.getUser().getFullName().isBlank()) {
        throw new IllegalArgumentException("Tài khoản người nhận chưa có thông tin chủ tài khoản");
    }

    return account.getUser().getFullName();
}

public BankAccount findByUser(User user) {
   return bankAccountRepository.findByUser(user)
            .orElseThrow(() ->
                new IllegalArgumentException("Không tìm thấy tài khoản")
            );
}

}
