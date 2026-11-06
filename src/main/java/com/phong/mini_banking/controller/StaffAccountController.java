package com.phong.mini_banking.controller;

import com.phong.mini_banking.service.StaffAccountService;
import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff/accounts")
public class StaffAccountController {
    private final StaffAccountService service;
    public StaffAccountController(StaffAccountService service) { this.service = service; }

    @PostMapping("/{id}/deposit")
    public String deposit(@PathVariable("id") Long id, @RequestParam(name = "amount", defaultValue = "") String amount,
                          @RequestParam(name = "requestId", defaultValue = "") String requestId,
                          Principal staff, RedirectAttributes flash) {
        try {
            service.deposit(id, new BigDecimal(amount.replace(".", "")), requestId, staff.getName());
            flash.addFlashAttribute("success", "Nạp tiền thành công. Số dư và lịch sử giao dịch đã được cập nhật.");
        } catch (NumberFormatException exception) {
            flash.addFlashAttribute("error", "Vui lòng nhập số tiền nạp hợp lệ bằng VNĐ.");
        } catch (IllegalArgumentException exception) {
            flash.addFlashAttribute("error", exception.getMessage());
        } catch (DataAccessException exception) {
            flash.addFlashAttribute("error", "Không thể ghi nhận giao dịch. Vui lòng kiểm tra số dư và tải lại trang trước khi thử lại.");
        }
        return "redirect:/staff";
    }

    @PostMapping("/{id}/status")
    public String status(@PathVariable("id") Long id, @RequestParam("status") String status,
                         Principal staff, RedirectAttributes flash) {
        try {
            service.setStatus(id, status, staff.getName());
            flash.addFlashAttribute("success", "LOCKED".equals(status)
                    ? "Đã khóa tài khoản khách hàng." : "Đã mở khóa tài khoản khách hàng.");
        } catch (IllegalArgumentException exception) {
            flash.addFlashAttribute("error", exception.getMessage());
        } catch (DataAccessException exception) {
            flash.addFlashAttribute("error", "Không thể cập nhật trạng thái. Vui lòng tải lại trang và thử lại.");
        }
        return "redirect:/staff";
    }
}
