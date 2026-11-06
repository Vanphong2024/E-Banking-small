package com.phong.mini_banking.repository;

import com.phong.mini_banking.entity.AccountStatusChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStatusChangeRepository extends JpaRepository<AccountStatusChange, Long> {}
