package com.phong.mini_banking.controller;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.phong.mini_banking.entity.BankAccount;
import com.phong.mini_banking.service.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
@RequestMapping("/bank-account")

public class BankAccountController {
 
    private final BankAccountService bankAccountService;
    public BankAccountController(BankAccountService bankAccountService)
    {
        this.bankAccountService = bankAccountService ;
    }
    @GetMapping
    public String getAllBankAccount(Model model) {
model.addAttribute(
        "bankAccount",
        bankAccountService.getAllBankAccount());
        return "bank-account-list";
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
    return "redirect:/bank-accounts"; 
}

    // 4 . hiển thị from sửa tài khoản 
    @GetMapping("/edit/{id}")
    public String showEditForm(
        @PathVariable Long  id,
    Model  model ) {
        BankAccount bankAccount = bankAccountService.getBankAccount(id);
   model.addAttribute("bankAccount",bankAccount);
    
    return "bank-account-from";
    }
    // 5 sử lý cập nhật tài khoản 
    @PostMapping("/edit/{id}")
    public String updateBankAccount(
        @PathVariable Long id, 
    @ModelAttribute ("bankAccount")BankAccount bankAccount) {
        //TODO: process POST request
        
        return "reditrect:/bank-accounts ";
    }
    // 6.xóa tài khoản 
    @GetMapping("/delete/{id}")
    public String deleteBankAccount (
        @PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);
        return "redirect:/bank-accounts";
    }
    
    
}
