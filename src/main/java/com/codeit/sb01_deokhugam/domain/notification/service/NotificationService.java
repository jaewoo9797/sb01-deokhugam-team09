package com.codeit.sb01_deokhugam.domain.notification.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.notification.exception.NotificationException;
import com.codeit.sb01_deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	@Transactional
	public NotificationDto confirmNotification(UUID notificationId, UUID userId) {
		Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
			.orElseThrow(() -> new NotificationException(ErrorCode.NOTIFICATION_NOT_FOUND));
		notification.markAsRead();
		return NotificationDto.of(notification);
	}

	public void confirmAllNotifications(UUID userId) {
		// TODO : 성능 개선하기 (데이터베이스에 유저의 알림이 많을 경우 락 걸림)
		notificationRepository.updateAllConfirmed(userId);
	}
}
