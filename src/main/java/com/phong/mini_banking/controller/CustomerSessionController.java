package com.phong.mini_banking.controller;

import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.service.CustomerSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerSessionController {
    private final CustomerSessionService sessions;

    public CustomerSessionController(CustomerSessionService sessions) {
        this.sessions = sessions;
    }

    @GetMapping("/customer/session-status")
    public ResponseEntity<String> status(HttpServletRequest request) {
        var session = request.getSession(false);
        String status = "expired";
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (Boolean.TRUE.equals(session.getAttribute(CustomerSessionService.SESSION_REPLACED_ATTRIBUTE))) {
                status = "replaced";
            } else if (user != null && sessions.isCurrentSession(user.getUserId(), session)) {
                status = "active";
            }
        }
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(status);
    }
}
