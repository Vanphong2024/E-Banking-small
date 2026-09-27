package com.phong.mini_banking.repository;
import com.phong.mini_banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.phong.mini_banking.entity.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {

      
    // hàm sinh xố ngần nhiên cho tài khoản
    boolean existsByAccountNumber(String accountNumber) ;
   // kiểm tra xem user đã có tài khoản chưa 
    boolean existsByUser_UserId(Long userId) ;
   // kiểm tra xem user đã có tài khoản chưa    
   boolean existsByUser(User user);
}

