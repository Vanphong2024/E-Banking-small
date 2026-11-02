package com.phong.mini_banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity 
public class SecurityConfig {
    
    @Bean 
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http)
    throws Exception{
        http
        .securityMatcher("/admin/**", "/staff/**", "/user/**", "/bank-account/**")
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/staff/create", "/bank-account/open-account/**")
                    .hasAuthority("STAFF_CUSTOMER_ACCOUNT")
                .requestMatchers("/staff/**", "/user/**", "/bank-account/**")
                    .hasRole("STAFF")
                .anyRequest().authenticated()
        )
        .formLogin(form -> form
              .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
                    response.sendRedirect(request.getContextPath()
                            + (isAdmin ? "/admin/dashboard" : "/staff"));
                })
                .permitAll()
        )
        .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(
                (request, response, exception) -> {
                    if (exception instanceof InvalidCsrfTokenException
                            && request.getRequestURI().equals(
                                    request.getContextPath() + "/admin/staff/create")) {
                        response.sendRedirect(request.getContextPath()
                                + "/admin/staff/create?csrfError");
                        return;
                    }
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }))
                .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout")
            );

        return http.build();
        }
@Bean 
@Order(2)
   

    public SecurityFilterChain customerSecurity(HttpSecurity http)
            throws Exception {

        http.authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
