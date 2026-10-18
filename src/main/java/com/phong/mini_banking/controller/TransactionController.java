package com.phong.mini_banking.controller;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.phong.mini_banking.*;
import com.phong.mini_banking.entity.transaction;
import com.phong.mini_banking.service.TransactionService;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.phong.mini_banking.service.UserService;
import com.phong.mini_banking.entity.User;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transaction;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/transactions")
public class TransactionController {
    
private final TransactionService transactionService;

public TransactionController (TransactionService   transactionService)
{
    this.transactionService =  transactionService;

}
//list user 
@GetMapping("/transfer")
public String showTransferForm() {
    return "transfer";
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
        HttpSession session,
        Model model) {

    User sender = (User) session.getAttribute("user");

    if (sender == null) {
        return "redirect:/customer/login";
    }

    try {
        transactionService.transferMoney(
            sender,
            receiverAccountNumber,
            amount
        );

        return "redirect:/transactions/success";

    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "transfer";
    }
}
    @GetMapping("/success")
    public String ShowSuccessPage(){
        return "transfer-success";
    }


}








