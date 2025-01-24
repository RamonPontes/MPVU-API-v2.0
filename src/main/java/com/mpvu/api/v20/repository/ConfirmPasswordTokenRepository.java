package com.mpvu.api.v20.repository;

import com.mpvu.api.v20.model.ConfirmPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmPasswordTokenRepository extends JpaRepository<ConfirmPasswordToken, Long> {
    Optional<ConfirmPasswordToken> findByToken(String token);
}
