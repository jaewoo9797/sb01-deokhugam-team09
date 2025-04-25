package com.codeit.sb01_deokhugam.domain.notification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query(value = "SELECT n FROM Notification n WHERE n.id= :id AND n.user.id = :userId")
	Optional<Notification> findByIdAndUserId(Long id, Long userId);

}
