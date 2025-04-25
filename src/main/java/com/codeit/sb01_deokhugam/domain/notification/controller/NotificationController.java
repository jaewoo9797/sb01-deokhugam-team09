package com.codeit.sb01_deokhugam.domain.notification.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationUpdateRequest;
import com.codeit.sb01_deokhugam.domain.notification.service.NotificationService;
import com.codeit.sb01_deokhugam.global.resolver.annotation.LoginUserId;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@PatchMapping("/{id}")
	public void updateConfirm(@PathVariable UUID id, @RequestBody NotificationUpdateRequest request, @LoginUserId UUID userId) {
		notificationService.confirmNotification(id, userId);
	}

	@PatchMapping("/read-all")
	public void updateConfirmAll() {

	}

	@GetMapping
	public void getNotifications(
		@RequestParam(name = "userId") UUID userId,
		@RequestParam(defaultValue = "DESC") String direction,
		@RequestParam(required = false) Instant cursor,
		@RequestParam(required = false) Instant after,
		@RequestParam(defaultValue = "20") int limit
	) {

	}
}
