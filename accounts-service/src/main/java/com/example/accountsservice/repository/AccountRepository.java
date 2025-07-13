package com.example.accountsservice.repository;

import com.example.accountsservice.model.AccountEntity;
import com.example.accountsservice.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByUser(UserEntity user);
}
