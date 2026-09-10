package com.phong.mini_banking.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phong.mini_banking.entity.User;
public interface UserRepository extends JpaRepository <User,Long> {

    
}
