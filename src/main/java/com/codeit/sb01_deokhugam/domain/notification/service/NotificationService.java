package com.codeit.sb01_deokhugam.domain.notification.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationSearchCondition;
import com.codeit.sb01_deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.notification.exception.NotificationException;
import com.codeit.sb01_deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.service.UserService;
import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

	private final UserService userService;
	private final NotificationRepository notificationRepository;

	public NotificationDto confirmNotification(UUID notificationId, UUID userId) {
		Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
			.orElseThrow(() -> new NotificationException(ErrorCode.NOTIFICATION_NOT_FOUND));
		notification.markAsRead();
		return NotificationDto.of(notification);
	}

	public void confirmAllNotifications(UUID userId) {
		// TODO : 트랜잭션 READ_ONLY 인 메서드를 호출할 경우 트랜잭션 안에서 전파 관련 개념 조사
		UserDto activeUser = userService.findActiveUser(userId);
		boolean hasNotificationsToConfirm = notificationRepository.existsByUserIdAndConfirmedFalse(activeUser.id());
		if (!hasNotificationsToConfirm) {
			return;
		}
		// TODO : 성능 개선하기 (데이터베이스에 유저의 알림이 많을 경우 락 걸림)
		notificationRepository.updateAllConfirmed(activeUser.id());
	}

	@Transactional(readOnly = true)
	public PageResponse<NotificationDto> getNotificationsByCursor(NotificationSearchCondition condition, int limit) {
		userService.findActiveUser(condition.getUserId());
		List<Notification> notifications = notificationRepository.findByCursorPagination(condition, limit);
		long totalCount = notificationRepository.countByUserIdAndConfirmedFalse(condition.getUserId());

		List<NotificationDto> content = notifications.stream().map(NotificationDto::of).toList();
		boolean hasNext = notifications.size() > limit;
		if (hasNext) {
			notifications.remove(notifications.size() - 1);
		}
		Instant nextCursor = getNextCursor(hasNext, notifications);
		int size = content.size();
		return new PageResponse<>(content, nextCursor, nextCursor, size, hasNext, totalCount);
	}

	private Instant getNextCursor(boolean hasNext, List<Notification> notifications) {
		if (hasNext) {
			return notifications.get(notifications.size() - 1).getCreatedAt();
		}
		return null;
	}
}
