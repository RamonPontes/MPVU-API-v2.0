package com.mpvu.api.v20.repository;

import com.mpvu.api.v20.model.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokensRepository extends JpaRepository<UserTokens, Integer> {
}
