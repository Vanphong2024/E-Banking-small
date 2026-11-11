package com.phong.mini_banking.config;

import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.repository.Adminrepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaffStatusInterceptorConfig implements WebMvcConfigurer {
    private final Adminrepository admins;

    public StaffStatusInterceptorConfig(Adminrepository admins) {
        this.admins = admins;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new StaffStatusInterceptor(admins))
                .addPathPatterns("/staff", "/staff/**");
    }

    private static class StaffStatusInterceptor implements HandlerInterceptor {
        private final Adminrepository admins;

        private StaffStatusInterceptor(Adminrepository admins) {
            this.admins = admins;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            response.setHeader("Pragma", "no-cache");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isStaff = authentication != null
                    && authentication.isAuthenticated()
                    && authentication.getAuthorities().stream()
                            .anyMatch(authority -> "ROLE_STAFF".equals(authority.getAuthority()));
            if (!isStaff) {
                return true;
            }

            boolean active = admins.findByEmail(authentication.getName())
                    .filter(admin -> admin.getRole() == Role.STAFF)
                    .map(admin -> "ACTIVE".equals(admin.getStatus()))
                    .orElse(false);
            if (active) {
                return true;
            }

            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
            SecurityContextHolder.clearContext();
            response.sendRedirect(request.getContextPath() + "/admin/login?accountLocked");
            return false;
        }
    }
}
