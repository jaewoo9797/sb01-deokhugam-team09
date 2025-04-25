package com.codeit.sb01_deokhugam.domain.notification.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeit.sb01_deokhugam.domain.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public void confirmNotification(UUID notificationId, UUID userId) {

	}
}
