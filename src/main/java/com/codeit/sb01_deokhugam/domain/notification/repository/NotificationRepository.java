package com.codeit.sb01_deokhugam.domain.notification.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID>, CursorNotificationRepository {

	@Query(value = "SELECT n FROM Notification n JOIN FETCH n.user u JOIN FETCH n.review r WHERE n.id = :id AND n.user.id = :userId")
	Optional<Notification> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE Notification n SET n.confirmed = TRUE WHERE n.user.id = :userId AND n.confirmed = false")
	int updateAllConfirmed(@Param("userId") UUID userId);

	List<Notification> findAllByUserId(UUID userId);

	// 알림이 확인되지 않고 계속해서 쌓여간다면 조회 시 복합 인덱스 고려
	boolean existsByUserIdAndConfirmedFalse(UUID userId);

	long countByUserIdAndConfirmedFalse(UUID userId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM Notification n WHERE n.confirmed = true AND n.updatedAt < :cutOfDate")
	int deleteConfirmedOlderThan(@Param("cutOfDate") Instant cutOfDate);

	void deleteByUserId(UUID pathId);
}
