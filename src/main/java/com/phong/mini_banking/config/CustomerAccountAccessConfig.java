package com.phong.mini_banking.config;

import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.repository.BankAccountRepository;
import com.phong.mini_banking.service.CustomerSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomerAccountAccessConfig implements WebMvcConfigurer {
    private final BankAccountRepository accounts;
    private final CustomerSessionService customerSessions;
    public CustomerAccountAccessConfig(BankAccountRepository accounts, CustomerSessionService customerSessions) {
        this.accounts = accounts;
        this.customerSessions = customerSessions;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomerAccountInterceptor(accounts, customerSessions))
                .addPathPatterns("/ebanking", "/transactions/**");
    }

    public static class CustomerAccountInterceptor implements HandlerInterceptor {
        private final BankAccountRepository accounts;
        private final CustomerSessionService customerSessions;
        public CustomerAccountInterceptor(BankAccountRepository accounts, CustomerSessionService customerSessions) {
            this.accounts = accounts;
            this.customerSessions = customerSessions;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            var session = request.getSession(false);
            User user = session == null ? null : (User) session.getAttribute("user");
            if (user == null || user.getRole() != Role.CUSTOMER) {
                boolean replaced = session != null
                        && Boolean.TRUE.equals(session.getAttribute(
                                CustomerSessionService.SESSION_REPLACED_ATTRIBUTE));
                response.sendRedirect(request.getContextPath()
                        + (replaced ? "/customer/login?sessionReplaced" : "/customer/login"));
                return false;
            }
            if (customerSessions.hasRegisteredSession(user.getUserId())
                    && !customerSessions.isCurrentSession(user.getUserId(), session)) {
                session.removeAttribute("user");
                response.sendRedirect(request.getContextPath() + "/customer/login?sessionReplaced");
                return false;
            }
            var account = accounts.findByUser(user);
            if (account.isEmpty() || !"ACTIVE".equals(account.get().getStatus())) {
                session.removeAttribute("user");
                response.sendRedirect(request.getContextPath() + "/customer/login?accountLocked");
                return false;
            }
            return true;
        }
    }
}
