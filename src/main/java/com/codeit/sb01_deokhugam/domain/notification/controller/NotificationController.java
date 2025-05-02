package com.codeit.sb01_deokhugam.domain.notification.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationSearchCondition;
import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationUpdateRequest;
import com.codeit.sb01_deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.sb01_deokhugam.domain.notification.service.NotificationService;
import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.resolver.annotation.LoginUserId;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@PatchMapping("/{id}")
	public ResponseEntity<NotificationDto> updateConfirm(
		@PathVariable UUID id,
		@RequestBody NotificationUpdateRequest request,
		@LoginUserId UUID userId
	) {
		NotificationDto notificationDto = notificationService.confirmNotification(id, userId);
		return ResponseEntity.ok(notificationDto);
	}

	@PatchMapping("/read-all")
	public ResponseEntity<Void> updateConfirmAll(@LoginUserId UUID userId) {
		notificationService.confirmAllNotifications(userId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<PageResponse<NotificationDto>> getNotifications(
		@RequestParam UUID userId,
		@RequestParam(defaultValue = "DESC") Sort.Direction direction,
		@RequestParam(required = false) Instant cursor,
		@RequestParam(required = false) Instant after,
		@RequestParam(defaultValue = "20") int limit,
		@LoginUserId UUID loginUserId
	) {
		NotificationSearchCondition condition = new NotificationSearchCondition(userId, direction, cursor, after);
		PageResponse<NotificationDto> notifications = notificationService.getNotificationsByCursor(condition, limit);
		return ResponseEntity.ok(notifications);
	}

}
