package com.phong.mini_banking.config;

import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.repository.BankAccountRepository;
import com.phong.mini_banking.service.CurrentCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class CustomerAccountAccessConfig implements WebMvcConfigurer {
    private final BankAccountRepository accounts;
    private final CurrentCustomerService currentCustomer;
    public CustomerAccountAccessConfig(
            BankAccountRepository accounts,
            CurrentCustomerService currentCustomer) {
        this.accounts = accounts;
        this.currentCustomer = currentCustomer;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomerAccountInterceptor(accounts, currentCustomer))
                .addPathPatterns("/ebanking", "/transactions/**", "/customer/profile");
    }

    public static class CustomerAccountInterceptor implements HandlerInterceptor {
        private final BankAccountRepository accounts;
        private final CurrentCustomerService currentCustomer;
        public CustomerAccountInterceptor(
                BankAccountRepository accounts,
                CurrentCustomerService currentCustomer) {
            this.accounts = accounts;
            this.currentCustomer = currentCustomer;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            var session = request.getSession(false);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isCustomer = authentication != null
                    && authentication.isAuthenticated()
                    && authentication.getAuthorities().stream()
                            .anyMatch(authority -> "ROLE_CUSTOMER".equals(authority.getAuthority()));
            if (!isCustomer) {
                response.sendRedirect(request.getContextPath() + "/customer/login");
                return false;
            }
            User user = currentCustomer.getCustomer(authentication);
            var account = accounts.findByUser(user);
            if (account.isEmpty() || !"ACTIVE".equals(account.get().getStatus())) {
                response.sendRedirect(request.getContextPath() + "/customer/login?accountLocked");
                return false;
            }
            return true;
        }
    }
}
