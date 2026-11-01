package com.phong.mini_banking.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.phong.mini_banking.entity.Admin;
import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.repository.Adminrepository;

@Service 
public class Adminservice implements UserDetailsService {
    // Implement các phương thức của UserService nếu cần thiết
    
private final Adminrepository adminRepository;

    public Adminservice(Adminrepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Thêm các phương thức để quản lý Admin nếu cần thiết
@Override 
   public  UserDetails loadUserByUsername(String email) 
   throws UsernameNotFoundException {
        Admin admin =adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
 // 2. Kiểm tra Role
        if (admin.getRole() != Role.ADMIN) {
            throw new UsernameNotFoundException("Tài khoản không hợp lệ");
        }




        // 3. Chuyển Admin thành UserDetails
        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .roles("ADMIN")
                .disabled(!"ACTIVE".equals(admin.getStatus()))
                .build();
    }


}