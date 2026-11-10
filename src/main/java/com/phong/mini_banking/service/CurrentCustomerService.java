package com.phong.mini_banking.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.repository.UserRepository;

@Service
public class CurrentCustomerService {
    private final UserRepository userRepository;

    public CurrentCustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCustomer(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {
            throw new UsernameNotFoundException("Customer chưa đăng nhập");
        }

        return userRepository.findByNormalizedEmail(authentication.getName().strip().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy Customer đang đăng nhập"));
    }
}
