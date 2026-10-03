package com.phong.mini_banking.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.service.UserService;
import jakarta.servlet.http.HttpSession;
@Controller 
public class LoginController {
  private final UserService userService;
  public LoginController(UserService userService){
    this.userService = userService;
  }
  @GetMapping("/customer/login")
  public String customerLoginForm() {
    return "customer-login";
//   }
//   @PostMapping("/customer/login")
//     public String customerLogin(
//         @RequestParam("email") String email,
//         @RequestParam("password") String password,
//         Model model) {
//         try {
//             User user =  userService.login(email, password);
// if (user.getRole()!=Role.CUSTOMER){
//     model.addAttribute("error","Tài khoản này không phải là tài khoản customer");
//        return "redirect:/customer-login";
//             }

//             return "redirect:/ebanking";

//         }
//          catch (RuntimeException e) {

//             model.addAttribute("error", e.getMessage());

//             return "customer-login";
//         }
//     }
}
@PostMapping("/customer/login")
public String customerLogin(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        HttpSession session,
        Model model) {

    System.out.println(">>> 1. LOGIN REQUEST");

    try {

        User user = userService.login(email, password);

        System.out.println(">>> 2. LOGIN SUCCESS");
        System.out.println(">>> 3. USER ROLE = " + user.getRole());

        if (user.getRole() != Role.CUSTOMER) {

          
            
            System.out.println(">>> 4. NOT CUSTOMER");

            model.addAttribute("error",
                    "Tài khoản này không phải là tài khoản customer");

            return "redirect:/customer/login";
        }
     // Lưu user vào session
            session.setAttribute("user", user);
        System.out.println(">>> 5. REDIRECT TO EBANKING");

        return "redirect:/ebanking";

    } catch (RuntimeException e) {

        System.out.println(">>> LOGIN ERROR = " + e.getMessage());

        model.addAttribute("error", e.getMessage());

        return "customer-login";
    }
}
}