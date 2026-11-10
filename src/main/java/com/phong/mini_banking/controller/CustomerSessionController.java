package com.phong.mini_banking.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerSessionController {
    @GetMapping("/customer/session-status")
    public ResponseEntity<String> status(
            HttpServletRequest request,
            Authentication authentication) {
        var session = request.getSession(false);
        String status = session != null
                && authentication != null
                && authentication.isAuthenticated()
                ? "active"
                : "expired";
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(status);
    }
}
