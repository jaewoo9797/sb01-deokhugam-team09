package com.codeit.sb01_deokhugam.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<User> findByIdAndIsDeletedFalse(UUID id);

	Optional<User> findByEmailAndIsDeletedFalse(String email);

	List<User> findAllByIsDeletedFalse();

}
