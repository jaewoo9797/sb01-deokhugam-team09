package com.codeit.sb01_deokhugam.domain.notification.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.notification.exception.NotificationException;
import com.codeit.sb01_deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.sb01_deokhugam.domain.review.entity.Review;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;
import com.codeit.sb01_deokhugam.util.TestUtils;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
	@InjectMocks
	private NotificationService notificationService;

	@Mock
	private NotificationRepository notificationRepository;

	private UUID notificationId;
	private UUID userId;
	private Notification notification;

	@BeforeEach
	void setUp() {
		notificationId = UUID.randomUUID();
		userId = UUID.randomUUID();

		User user = new User("test@email.com", "pw", "닉네임");
		Book book = getBook();
		Review review = new Review(user, book, "좋아요", BigDecimal.valueOf(4.0));

		notification = Notification.fromComment(user, "댓글 내용", review);
		TestUtils.setId(notification, notificationId);
	}

	private static Book getBook() {
		return new Book(
			"이펙티브 자바",
			"조슈아 블로크",
			"자바 모범 사례를 담은 책입니다.",
			"9780134685991",
			"한빛미디어",
			LocalDate.of(2018, 1, 1),
			"https://example.com/thumbnail.jpg",
			10,
			new BigDecimal("4.8"),
			false
		);
	}

	@Test
	@DisplayName("알림 확인 성공 시 DTO 반환")
	void confirmNotification_Success() {
		// given
		given(notificationRepository.findByIdAndUserId(notificationId, userId))
			.willReturn(Optional.of(notification));

		// when
		NotificationDto dto = notificationService.confirmNotification(notificationId, userId);

		// then
		assertThat(dto.id()).isEqualTo(notificationId);
		assertThat(dto.confirmed()).isTrue();
		verify(notificationRepository).findByIdAndUserId(notificationId, userId);
	}

	@DisplayName("알림이 없으면 예외 발생")
	@Test
	void confirmNotification_ThrowsException_WhenNotFound() {
		//given
		given(notificationRepository.findByIdAndUserId(notificationId, userId))
			.willReturn(Optional.empty());

		// when then
		assertThatThrownBy(() -> notificationService.confirmNotification(notificationId, userId))
			.isInstanceOf(NotificationException.class)
			.hasMessageContaining(ErrorCode.NOTIFICATION_NOT_FOUND.getMessage());
		verify(notificationRepository).findByIdAndUserId(notificationId, userId);
	}
}
