package com.phong.mini_banking.controller;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.phong.mini_banking.*;
import com.phong.mini_banking.entity.transaction;
import com.phong.mini_banking.service.BankAccountService;
import com.phong.mini_banking.service.TransactionService;
import java.util.List;
import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.phong.mini_banking.service.UserService;
import com.phong.mini_banking.entity.BankAccount;
import com.phong.mini_banking.entity.User;
import jakarta.transaction.Transaction;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import com.phong.mini_banking.service.CurrentCustomerService;
@Controller
@RequestMapping("/transactions")
public class TransactionController {
    private final BankAccountService bankAccountService;
private final TransactionService transactionService;
private final CurrentCustomerService currentCustomer;

public TransactionController (TransactionService   transactionService ,
     BankAccountService bankAccountService,
     CurrentCustomerService currentCustomer
    
)
{
    this.transactionService =  transactionService;
    this.bankAccountService = bankAccountService;
    this.currentCustomer = currentCustomer;
    }
    @GetMapping("/transfer")
    public String showTransferForm(Authentication authentication, Model model) {
        User user = currentCustomer.getCustomer(authentication);

    BankAccount bankAccount = bankAccountService.findByUser(user);
    List<transaction> transactions =
            transactionService.getTransactionHistory(bankAccount.getAccountId());

    model.addAttribute("transactions", transactions);
    model.addAttribute("account", bankAccount);
    model.addAttribute("currentAccountId", bankAccount.getAccountId());

    return "transfer";
}

@GetMapping("/receiver-name")
@ResponseBody
public Map<String, String> getReceiverName(
        @RequestParam("accountNumber") String accountNumber) {
    return Map.of("receiverName", bankAccountService.getReceiverName(accountNumber));
}

@GetMapping("/{id}")
public String getTransactionById(
    @PathVariable Long id , Model model){
transaction transaction = transactionService.getTransactionById(id);
model.addAttribute("transaction",transaction);
return "transaction-detail";    }
//Post : thêm trasction 
@PostMapping("/transfer")
public String transfer(
     @RequestParam("receiverAccountNumber") String receiverAccountNumber,
        @RequestParam("amount") BigDecimal amount,
        Authentication authentication,
        RedirectAttributes flash) {

    User sender = currentCustomer.getCustomer(authentication);

    try {
        transactionService.transferMoney(
            sender,
            receiverAccountNumber,
            amount
        );

        return "redirect:/transactions/success";

    } catch (IllegalArgumentException e) {
        flash.addFlashAttribute("error", e.getMessage());
        return "redirect:/transactions/transfer";
    } catch (org.springframework.dao.DataAccessException e) {
        flash.addFlashAttribute("error", "Tài khoản vừa được cập nhật. Vui lòng kiểm tra lịch sử và thử lại.");
        return "redirect:/transactions/transfer";
    }
}
    @GetMapping("/success")
    public String ShowSuccessPage(){
        return "transfer-success";
    }

 
@GetMapping("/history")
public String showHistory(Authentication authentication, Model model) {
    User user = currentCustomer.getCustomer(authentication);

    BankAccount bankAccount = bankAccountService.findByUser(user);

    List<transaction> transactions = transactionService.getTransactionHistory(bankAccount.getAccountId());
 // Kiểm tra dữ liệu backend lấy được
    System.out.println("Session user ID: " + user.getUserId());
    System.out.println("Account ID: " + bankAccount.getAccountId());
    System.out.println("Số giao dịch: " + transactions.size());

model.addAttribute("transactions", transactions);
model.addAttribute("account", bankAccount);
model.addAttribute("currentAccountId", bankAccount.getAccountId());

    return "transfer";
}

}






