package com.phong.mini_banking.controller;

import com.phong.mini_banking.service.CurrentCustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerProfileController {
    private final CurrentCustomerService currentCustomer;

    public CustomerProfileController(CurrentCustomerService currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    @GetMapping("/customer/profile")
    public String showProfile(Authentication authentication) {
        currentCustomer.getCustomer(authentication);
        return "redirect:/ebanking?section=account-information#account-information";
    }
}
