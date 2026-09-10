package com.phong.mini_banking.controller;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // List User
    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    // Find User theo ID
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {

        Optional<User> user = userService.getUsersById(id);

        model.addAttribute("user", user);

        return "user-detail";
    }
@GetMapping("/create")
public String showCreateFrom(Model model) {
model.addAttribute("user",new User());
return "user-form";

}

    // Add User
    @PostMapping
    public String createUser(@ModelAttribute User user) {

        userService.createUser(user);

        return "redirect:/users";
    }

    // Update User
    @PostMapping("/save")
    public String updateUser(
          
            @ModelAttribute User user) {
        userService.updateUser(user);

        return "redirect:/users";
    }

    // Delete User
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return "redirect:/users";
    }
}