package com.phong.mini_banking.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.phong.mini_banking.entity.Admin;
import com.phong.mini_banking.entity.Role;
import com.phong.mini_banking.repository.Adminrepository;
import java.util.List;

@Service 
public class Adminservice implements UserDetailsService {
    // Implement các phương thức của UserService nếu cần thiết
    
private final Adminrepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Adminservice(Adminrepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Admin> getStaff() {
        return adminRepository.findByRole(Role.STAFF);
    }

    public Admin createStaff(String fullName, String email, String password) {
        fullName = fullName.trim();
        email = email.trim();

        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin Staff");
        }

        if (adminRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email Staff đã tồn tại");
        }

        Admin staff = new Admin();
        staff.setFullName(fullName);
        staff.setEmail(email);
        staff.setPassword(passwordEncoder.encode(password));
        staff.setRole(Role.STAFF);
        staff.setStatus("ACTIVE");
        staff.setCanManageCustomerAccounts(false);
        try {
            return adminRepository.saveAndFlush(staff);
        } catch (DataAccessException exception) {
            throw new IllegalArgumentException(
                    "Không thể tạo Staff. Kiểm tra cấu trúc database hoặc email đã tồn tại.",
                    exception);
        }
    }

    public Admin toggleStaffStatus(Long staffId) {
        Admin staff = adminRepository.findById(staffId)
                .filter(account -> account.getRole() == Role.STAFF)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Staff"));
        staff.setStatus("ACTIVE".equals(staff.getStatus()) ? "LOCKED" : "ACTIVE");
        return adminRepository.save(staff);
    }

    public Admin toggleCustomerAccountPermission(Long staffId) {
        Admin staff = adminRepository.findById(staffId)
                .filter(account -> account.getRole() == Role.STAFF)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Staff"));
        staff.setCanManageCustomerAccounts(!staff.isCanManageCustomerAccounts());
        return adminRepository.save(staff);
    }

    // Thêm các phương thức để quản lý Admin nếu cần thiết
@Override 
   public  UserDetails loadUserByUsername(String email) 
   throws UsernameNotFoundException {
        Admin admin =adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
 // 2. Kiểm tra Role
        if (admin.getRole() != Role.ADMIN && admin.getRole() != Role.STAFF) {
            throw new UsernameNotFoundException("Tài khoản không hợp lệ");
        }




        // 3. Chuyển Admin thành UserDetails
        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .authorities(
                        "ROLE_" + admin.getRole().name(),
                        admin.isCanManageCustomerAccounts()
                                ? "STAFF_CUSTOMER_ACCOUNT"
                                : "STAFF_NO_CUSTOMER_ACCOUNT")
                .disabled(!"ACTIVE".equals(admin.getStatus())
                        || (admin.getRole() == Role.STAFF
                                && !admin.isCanManageCustomerAccounts()))
                .build();
    }


}