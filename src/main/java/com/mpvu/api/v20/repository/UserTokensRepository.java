package com.mpvu.api.v20.repository;

import com.mpvu.api.v20.model.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokensRepository extends JpaRepository<UserTokens, Integer> {
    Optional<UserTokens> findByToken(String token);
}
