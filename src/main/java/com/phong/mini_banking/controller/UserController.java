package com.phong.mini_banking.controller;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/staff")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // List User
    @GetMapping
    public String getAllUsers(Model model) {
        var users = userService.getAllUsers();
        model.addAttribute("users", users);
        java.util.Map<Long, String> depositRequestIds = new java.util.HashMap<>();
        for (User user : users) {
            if (user.getBankAccount() != null) {
                depositRequestIds.put(user.getBankAccount().getAccountId(), java.util.UUID.randomUUID().toString());
            }
        }
        model.addAttribute("depositRequestIds", depositRequestIds);
        return "staff";
    }

    // Find User theo ID
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {

        Optional<User> user = userService.getUsersById(id);

        model.addAttribute("User", user);

        return "redirect:/staff";
    }
@GetMapping("/create")
public String showCreateFrom(Model model) {
model.addAttribute("user",new User());
return "staff-form";

}
    // Add User
    @PostMapping ("/create")
    public String createUser(@ModelAttribute User user, Model model) {
        user.setRole(Role.CUSTOMER);
        try {
        userService.createUser(user);
        return "redirect:/staff";
        } catch (IllegalArgumentException exception) {
            user.setPassword("");
            model.addAttribute("error", exception.getMessage());
            return "staff-form";
        }
    }

    // // Update User
    // @PostMapping("/save")
    // public String updateUser(     
    //         @ModelAttribute User user) {
    //     userService.updateUser(user);

    //     return "redirect:/user";
    // }
    

    // // Delete User
    // @PostMapping("/{id}/delete")
    // public String deleteUser(@PathVariable Long id) {

    //     userService.deleteUser(id);

    //     return "redirect:/users";
    // }

    
}
