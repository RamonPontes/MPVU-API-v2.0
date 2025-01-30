package com.mpvu.api.v20.repository;

import com.mpvu.api.v20.model.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTokensRepository extends JpaRepository<UserTokens, Integer> {
    Optional<UserTokens> findByToken(String token);
    Optional<UserTokens> findByUserIdAndPlatform(UUID userId, String platform);
}
