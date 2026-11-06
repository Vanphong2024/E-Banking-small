package com.phong.mini_banking.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.service.UserService;
import com.phong.mini_banking.service.CustomerSessionService;
import com.phong.mini_banking.repository.BankAccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
@Controller 
public class LoginController {
  private final UserService userService;
  private final BankAccountRepository bankAccountRepository;
  private final CustomerSessionService customerSessionService;
  @Autowired
  public LoginController(UserService userService, BankAccountRepository bankAccountRepository,
          CustomerSessionService customerSessionService){
    this.userService = userService;
    this.bankAccountRepository = bankAccountRepository;
    this.customerSessionService = customerSessionService;
  }

  public LoginController(UserService userService, BankAccountRepository bankAccountRepository) {
    this(userService, bankAccountRepository, new CustomerSessionService());
  }
  @GetMapping({"/", "/login", "/customer-login"})
  public String customerLoginRedirect() {
    return "redirect:/customer/login";
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
//     model.addAttribute("error","Tài khoản này chưa hoạt động được.");
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
@PostMapping({"/customer/login", "/login", "/customer-login"})
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
                    "Tài khoản này chưa hoạt động được.");

            return "customer-login";
        }
        var account = bankAccountRepository.findByUser(user);
        if (account.isEmpty()) {
            model.addAttribute("error",
                    "Bạn chưa được mở tài khoản ngân hàng. Vui lòng liên hệ nhân viên để mở tài khoản.");
            return "customer-login";
        }
        if (!"ACTIVE".equals(account.get().getStatus())) {
            session.removeAttribute("user");
            model.addAttribute("error", "Tài khoản ngân hàng đang bị khóa. Vui lòng liên hệ nhân viên.");
            return "customer-login";
        }
     // Lưu user vào session
            customerSessionService.register(user.getUserId(), session);
            session.setAttribute("user", user);
        System.out.println(">>> 5. REDIRECT TO EBANKING");

        return "redirect:/ebanking";

    } catch (RuntimeException e) {

        System.out.println(">>> LOGIN ERROR = " + e.getMessage());

        model.addAttribute("error", e.getMessage());

        return "customer-login";
    }

    }

    @GetMapping("/customer/logout")
    public String customerLogout(HttpSession session) {
        customerSessionService.unregister(session);
        session.invalidate();
        return "redirect:/customer/login?logout";
    }
}
