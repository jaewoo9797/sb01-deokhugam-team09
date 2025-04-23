package com.codeit.sb01_deokhugam.domain.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsByEmail(String email);
	boolean existsByNickname(String nickname);
}
