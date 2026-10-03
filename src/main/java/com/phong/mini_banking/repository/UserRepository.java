package com.phong.mini_banking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.phong.mini_banking.entity.User;

public interface UserRepository extends JpaRepository <User,Long> {
// tìm kiếm user theo email 
    Optional<User> findByEmail(String email);
}
