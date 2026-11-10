package com.phong.mini_banking.controller;
import com.phong.mini_banking.entity.BankAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.repository.BankAccountRepository;
import com.phong.mini_banking.service.CurrentCustomerService;
import org.springframework.security.core.Authentication;
@Controller 
public class EBankingController {
 private final BankAccountRepository bankAccountRepository;
 private final CurrentCustomerService currentCustomer;
 public EBankingController(
       BankAccountRepository bankAccountRepository,
       CurrentCustomerService currentCustomer){
    this.bankAccountRepository = bankAccountRepository;
    this.currentCustomer = currentCustomer;
  }
  @GetMapping("/ebanking")
  public String showEbankingPage(Model model, Authentication authentication) {
    User user = currentCustomer.getCustomer(authentication);
  
  // tìm tài khoản ngân hàng theo id của user 
  BankAccount bankAccount = bankAccountRepository.
  findByUser(user).orElseThrow(() -> 
  new RuntimeException("user chưa có tài khoản ngân hàng "));
  //lưu lại sang ebanking.html
  model.addAttribute("user", user);
  model.addAttribute("account",bankAccount);
  return "ebanking";
  }

}