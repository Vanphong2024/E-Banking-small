package com.phong.mini_banking.controller;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.phong.mini_banking.*;
import com.phong.mini_banking.entity.transaction;
import com.phong.mini_banking.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.phong.mini_banking.service.UserService;

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
@GetMapping
public String getAllTransaction(Model model){
 model.addAttribute("Transaction",transactionService.getAllTransactions());
 return "Transactions"; 
}
// find user by id 
@GetMapping("/{id}")
public String getTransactionById(
    @PathVariable Long id , Model model){
transaction transaction = transactionService.getTransactionById(id);
model.addAttribute("transaction",transaction);
return "transaction-dentail";    }
//Post : thêm trasction 
@PostMapping 
public String savetransaction(@ModelAttribute transaction transaction) {
    transactionService.saveTransaction(transaction);
    
    //TODO: process POST request
    
    return "redirect:/transactions";

}
// Get : hiển thị from sửa  transaction 
@GetMapping("/create")
public String showCreateFrom (Model model ) {
   model .addAttribute("transaction",new transaction ());
    return "trasaction-from";

}

//Get: hiển thị from sửa transaction 
@GetMapping("/edit/{id}")
public String showEditFrom(
    @PathVariable Long id,
    Model model ){
        transaction transaction = transactionService.getTransactionById(id);
        model.addAttribute("transaction",transaction);
        return "trasaction-from";    }

        // Put : cập nhât transaction 
        @PutMapping("/{id}")
        public String updateTransaction( 
            @ModelAttribute transaction transaction) {
 
            transactionService.updateTransaction(transaction);

            //TODO: process PUT request
            
            return "redirect:/transactions";
        }
        //Detlet : xóa transaction 
        @DeleteMapping("/{id}")
        public String deletTransaction (@PathVariable Long id){
            transactionService.deleteTransaction(id);
            return "redirect:/transactions";
        }
    }









