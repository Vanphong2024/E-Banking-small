package com.phong.mini_banking.controller;
import java.util.Map;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phong.mini_banking.entity.BankAccount;
import com.phong.mini_banking.service.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.repository.UserRepository;
@Controller
@RequestMapping("/bank-account")

public class BankAccountController {
 
    private final BankAccountService bankAccountService;
    private final UserRepository userRepository;

    public BankAccountController(BankAccountService bankAccountService, UserRepository userRepository) {
        this.bankAccountService = bankAccountService;
        this.userRepository = userRepository;
    }
    @GetMapping
    public String getAllBankAccount(Model model) {
model.addAttribute(
        "accounts",
        bankAccountService.getAllBankAccount());
        return "back-account-list";
    }
//2: hiển thị fromt thêm tài khoanr 
@GetMapping("/create")
public String showCreateFrom(Model model) {
    model.addAttribute("bankAccount",new BankAccount());
    return "bank-account-form";
}
// 3 sử lý thêm tài khoản 
@PostMapping("/create")
public String createBankAccount 
(@ModelAttribute ("bankAccount")BankAccount bankAccount) {
    //TODO: process POST request
    bankAccountService.saveBankAccount(bankAccount);
    return "redirect:/bank-account"; 
}


// gửi yêu cầu tạo tài khoản 
@PostMapping("/open-account/{userId}")
public String openAccount(@PathVariable ("userId") Long userId){
    System.out.println(">>> OPEN ACCOUNT - userId = " + userId);
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    BankAccount bankAccount = bankAccountService.openAccount(user);
    return "redirect:/staff"; 

}
@GetMapping("/receiver-name")
@ResponseBody
public Map<String, String> getReceiverName(
        @RequestParam("accountNumber") String accountNumber) {

    String receiverName =
            bankAccountService.getReceiverName(accountNumber);

    return Map.of("receiverName", receiverName);
}
}    