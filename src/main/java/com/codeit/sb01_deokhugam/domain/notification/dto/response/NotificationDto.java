package com.codeit.sb01_deokhugam.domain.notification.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;

public record NotificationDto(
	UUID id,
	UUID userId,
	UUID reviewId,
	String reviewTitle,
	String content,
	boolean confirmed,
	Instant createdAt,
	Instant updatedAt
) {
	public static NotificationDto of(Notification notification) {
		return new NotificationDto(
			notification.getId(),
			notification.getUser().getId(),
			notification.getReview().getId(),
			notification.getReview().getContent(), // review title == review.content
			notification.getContent(),
			notification.isConfirmed(),
			notification.getCreatedAt(),
			notification.getUpdatedAt()
		);
	}
}
