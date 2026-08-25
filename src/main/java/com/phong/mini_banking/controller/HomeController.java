package com.phong.mini_banking.controller;
import org.springframework  .ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
  @Controller
public class HomeController {



    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("name", "David");

        return "index";
    }
}  

