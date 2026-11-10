package com.phong.mini_banking.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller 
public class LoginController {
  @GetMapping({"/", "/login", "/customer-login"})
  public String customerLoginRedirect() {
    return "redirect:/customer/login";
  }

  @GetMapping("/customer/login")
  public String customerLoginForm() {
    return "customer-login";
  }
}
