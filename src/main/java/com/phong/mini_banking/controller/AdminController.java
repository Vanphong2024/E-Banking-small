package com.phong.mini_banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.phong.mini_banking.service.Adminservice;
import com.phong.mini_banking.entity.Admin;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.dao.DataAccessException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller 
@RequestMapping("/admin")
public class AdminController {
    private final Adminservice adminService;

    public AdminController(Adminservice adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login")
    public String login() {
        return "admin-login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStaff", adminService.getStaff().size());
        return "admin";
    }

    @GetMapping("/staff")
    public String staff(Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        model.addAttribute("staff", adminService.getStaff());
        return "admin-staff";
    }

    @GetMapping("/staff/create")
    public String createStaffForm(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        return "admin-staff-form";
    }

    @PostMapping("/staff/create")
    public String createStaff(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        try {
            adminService.createStaff(fullName, email, password);
            return "redirect:/admin/staff";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            return "admin-staff-form";
        }
    }

    @PostMapping("/staff/{id}/toggle-status")
    public String toggleStaffStatus(@PathVariable("id") Long id, RedirectAttributes flash) {
        try {
            Admin staff = adminService.toggleStaffStatus(id);
            flash.addFlashAttribute("success",
                    "ACTIVE".equals(staff.getStatus()) ? "Đã mở khóa Staff." : "Đã khóa Staff.");
        } catch (IllegalArgumentException | DataAccessException exception) {
            flash.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @PostMapping("/staff/{id}/toggle-customer-account-permission")
    public String toggleCustomerAccountPermission(@PathVariable("id") Long id, RedirectAttributes flash) {
        try {
            adminService.toggleCustomerAccountPermission(id);
            flash.addFlashAttribute("success", "Đã cập nhật quyền quản lý tài khoản khách hàng.");
        } catch (IllegalArgumentException | DataAccessException exception) {
            flash.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @GetMapping
    public String home() {
        return "redirect:/admin/dashboard";
    }

}
