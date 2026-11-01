package com.phong.mini_banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phong.mini_banking.entity.Admin;

@Repository
public interface Adminrepository extends JpaRepository<Admin, Long> {
 
       Optional<Admin> findByEmail(String email);

}
