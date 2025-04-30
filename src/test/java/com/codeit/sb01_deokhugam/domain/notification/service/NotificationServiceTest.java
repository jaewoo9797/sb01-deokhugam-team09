package com.codeit.sb01_deokhugam.domain.notification.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationSearchCondition;
import com.codeit.sb01_deokhugam.domain.notification.dto.response.NotificationDto;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.notification.exception.NotificationException;
import com.codeit.sb01_deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.sb01_deokhugam.domain.review.entity.Review;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.service.UserService;
import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;
import com.codeit.sb01_deokhugam.util.EntityProvider;
import com.codeit.sb01_deokhugam.util.TestUtils;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@InjectMocks
	private NotificationService notificationService;

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private UserService userService;

	private Notification notification;
	private User user;
	private Review review;

	@BeforeEach
	void setUp() {
		user = EntityProvider.createUser();
		Book book = EntityProvider.createBook();
		review = EntityProvider.createReview(user, book);

		notification = Notification.fromComment(user, "댓글 내용", review);
		TestUtils.setId(notification, UUID.randomUUID());
	}

	@Test
	@DisplayName("알림 확인 성공 시 DTO 반환")
	void confirmNotification_Success() {
		// given
		given(notificationRepository.findByIdAndUserId(notification.getId(), user.getId()))
			.willReturn(Optional.of(notification));

		// when
		NotificationDto dto = notificationService.confirmNotification(notification.getId(), user.getId());

		// then
		assertThat(dto.id()).isEqualTo(notification.getId());
		assertThat(dto.confirmed()).isTrue();
		verify(notificationRepository).findByIdAndUserId(notification.getId(), user.getId());
	}

	@DisplayName("알림이 없으면 예외 발생")
	@Test
	void confirmNotification_ThrowsException_WhenNotFound() {
		//given
		given(notificationRepository.findByIdAndUserId(notification.getId(), user.getId()))
			.willReturn(Optional.empty());

		// when then
		assertThatThrownBy(() -> notificationService.confirmNotification(notification.getId(), user.getId()))
			.isInstanceOf(NotificationException.class)
			.hasMessageContaining(ErrorCode.NOTIFICATION_NOT_FOUND.getMessage());
		verify(notificationRepository).findByIdAndUserId(notification.getId(), user.getId());
	}

	@DisplayName("알림이 존재하는 유저는 업데이트 메서드가 호출된다.")
	@Test
	void CallUpdateAllConfirmed_when_exists() {
		//given
		UUID userId = UUID.randomUUID();
		UserDto userDto = new UserDto(userId, "test@email.com", "nickname", Instant.now());

		when(userService.findActiveUser(userId))
			.thenReturn(userDto);

		when(notificationRepository.existsByUserIdAndConfirmedFalse(userId))
			.thenReturn(true);

		// when
		notificationService.confirmAllNotifications(userId);

		// then
		verify(notificationRepository).existsByUserIdAndConfirmedFalse(userId);
		verify(notificationRepository).updateAllConfirmed(userId);
	}

	@DisplayName("알림이 존재하지 않는 유저는 업데이트 메서드가 호출되지 않는다.")
	@Test
	void doesNotCallUpdateAllConfirmed_whenNoNotificationsExist() {
		//given
		UUID userId = UUID.randomUUID();
		UserDto userDto = new UserDto(userId, "test@email.com", "nickname", Instant.now());

		when(userService.findActiveUser(userId))
			.thenReturn(userDto);

		when(notificationRepository.existsByUserIdAndConfirmedFalse(userId))
			.thenReturn(false);

		// when
		notificationService.confirmAllNotifications(userId);

		// then
		verify(notificationRepository, never()).updateAllConfirmed(userId);
	}

	@DisplayName("다음 페이지가 존재하면 hasNext=true, nextCursor 설정됨")
	@Test
	void should_return_hasNext_true_and_nextCursor_when_notifications_exceed_limit() {
		//given
		int totalCount = 6;
		int limit = 3;
		List<Notification> notifications = createNotificationsWithCreatedAt(totalCount);
		NotificationSearchCondition condition = new NotificationSearchCondition(user.getId(), Sort.Direction.DESC, null, null);

		given(notificationRepository.findByCursorPagination(condition, limit))
			.willReturn(notifications);
		given(notificationRepository.countByUserIdAndConfirmedFalse(user.getId()))
			.willReturn((long)totalCount);

		// when
		PageResponse<NotificationDto> result = notificationService.getNotificationsByCursor(condition, limit);

		// then
		assertThat(result.isHasNext()).isTrue();
		assertThat(result.getNextCursor()).isNotNull();
	}

	private List<Notification> createNotificationsWithCreatedAt(int count) {
		List<Notification> notifications = new ArrayList<>();
		Instant baseTime = Instant.now();

		for (int i = 0; i < count; i++) {
			Notification n = Notification.fromLike(user, review);
			Instant createdAt = baseTime.minusSeconds(i * 60L); // 1분 간격으로 생성 시간 차이
			TestUtils.setField(n, "createdAt", createdAt);
			notifications.add(n);
		}

		return notifications;
	}

}
