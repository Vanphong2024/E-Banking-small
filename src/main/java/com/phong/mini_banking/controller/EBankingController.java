package com.phong.mini_banking.controller;
import com.phong.mini_banking.entity.BankAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;    
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.repository.BankAccountRepository;
@Controller 
public class EBankingController {
 private final BankAccountRepository bankAccountRepository;
 public EBankingController(BankAccountRepository bankAccountRepository){
    this.bankAccountRepository = bankAccountRepository;
  }
  @GetMapping("/ebanking")
  public String showEbankingPage(Model model , HttpSession session) {
    User user = (User) session.getAttribute("user");

    //nếu user chưa đăng nhập
    if (user == null) {
        return "redirect:/customer/login";
    }
  
  // tìm tài khoản ngân hàng theo id của user 
  BankAccount bankAccount = bankAccountRepository.
  findByUser(user).orElseThrow(() -> 
  new RuntimeException("user chưa có tài khoản ngân hàng "));
  //lưu lại sang ebanking.html
  model.addAttribute("account",bankAccount);
  return "ebanking";
  }

}