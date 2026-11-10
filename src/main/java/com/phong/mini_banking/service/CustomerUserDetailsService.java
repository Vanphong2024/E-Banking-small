package com.phong.mini_banking.service;

import java.util.Locale;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.phong.mini_banking.entity.User;
import com.phong.mini_banking.repository.BankAccountRepository;
import com.phong.mini_banking.repository.UserRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public CustomerUserDetailsService(
            UserRepository userRepository,
            BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User customer = userRepository
                .findByNormalizedEmail(email.strip().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UsernameNotFoundException("Customer không tồn tại"));

        if (customer.getRole() != com.phong.mini_banking.entity.Role.CUSTOMER) {
            throw new UsernameNotFoundException("Tài khoản không phải Customer");
        }

        boolean accountActive = bankAccountRepository.findByUser(customer)
                .map(account -> "ACTIVE".equals(account.getStatus()))
                .orElse(false);

        return org.springframework.security.core.userdetails.User
                .withUsername(customer.getEmail())
                .password(customer.getPassword())
                .roles("CUSTOMER")
                .disabled(!accountActive)
                .build();
    }
}
