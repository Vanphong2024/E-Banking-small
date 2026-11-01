package com.phong.mini_banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity 
public class SecurityConfig {
    
    @Bean 
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http)
    throws Exception{
        http
        .securityMatcher("/admin/**", "/user", "/user/**")
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/**", "/user", "/user/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        )
        .formLogin(form -> form
              .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/user", true)
                .permitAll()
        )
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

