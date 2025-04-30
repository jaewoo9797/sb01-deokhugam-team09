package com.codeit.sb01_deokhugam.domain.notification.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.codeit.sb01_deokhugam.config.JpaAuditingConfiguration;
import com.codeit.sb01_deokhugam.config.QueryDslConfig;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationSearchCondition;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.review.entity.Review;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.util.EntityProvider;

import groovy.util.logging.Log4j2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("test")
@Import({QueryDslConfig.class, JpaAuditingConfiguration.class})
@Log4j2
class NotificationRepositoryTest {

	private static final Logger log = LogManager.getLogger(NotificationRepositoryTest.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NotificationRepository notificationRepository;

	private User user;
	private Review review;

	@BeforeEach
	void setUp() {
		user = EntityProvider.createUser();
		Book book = EntityProvider.createBook();
		review = EntityProvider.createReview(user, book);
		entityManager.persist(user);
		entityManager.persist(book);
		entityManager.persist(review);
		entityManager.flush();
		entityManager.clear();
	}

	@DisplayName("findByIdAndUserId: 알림 ID와 유저 ID 로 알림을 조회한다.")
	@Test
	void shouldReturnNotification_WhenExists() {
		//given
		Notification notification = Notification.fromComment(user, "좋은 책입니다.", review);
		entityManager.persist(notification);
		entityManager.flush();
		entityManager.clear();

		// when
		Notification foundNotification = notificationRepository.findByIdAndUserId(notification.getId(), user.getId()).orElse(null);

		// then
		assertAll(
			() -> {
				assertThat(foundNotification).isNotNull();
				assertThat(foundNotification.getUser().getId()).isEqualTo(user.getId());
				assertThat(foundNotification.getReview().getId()).isEqualTo(review.getId());
			}
		);
	}

	@DisplayName("findByIdAndUserId: 알림 ID와 유저 ID 로 알림을 조회한다. 알림이 존재하지 않을 경우 빈 Optional 을 반환한다.")
	@Test
	void findByIdAndUserId_ShouldReturnEmpty_WhenNotificationDoesNotExist() {
		// given
		UUID nonexistentNotificationId = UUID.randomUUID();
		UUID nonexistentUserId = UUID.randomUUID();

		// when
		Optional<Notification> result = notificationRepository.findByIdAndUserId(nonexistentNotificationId, nonexistentUserId);

		// then
		assertThat(result).isEmpty();
	}

	@DisplayName("updateAllConfirmed: 유저 ID로 모든 알림을 읽음 처리한다.")
	@Test
	void given_userId_when_updateAll_confirm() {
		//given
		for (int i = 0; i < 5; i++) {
			Notification notification = Notification.fromLike(user, review);
			entityManager.persist(notification);
		}
		entityManager.flush();
		entityManager.clear();

		// when
		int updateRowCount = notificationRepository.updateAllConfirmed(user.getId());
		List<Boolean> notificationConfirm = notificationRepository.findAllByUserId(user.getId())
			.stream()
			.map(Notification::isConfirmed).toList();

		// then
		assertThat(updateRowCount).isEqualTo(5);
		assertThat(notificationConfirm).containsExactly(true, true, true, true, true);
	}

	@DisplayName("existsByUserIdAndConfirmedFalse: 유저 ID로 읽지 않은 알림이 존재하는지 확인한다.")
	@Test
	void givenUserId_whenExistsUnreadNotifications_thenReturnTrue() {
		//given
		Notification notification = Notification.fromLike(user, review);
		entityManager.persist(notification);
		entityManager.flush();
		entityManager.clear();
		// when
		boolean exists = notificationRepository.existsByUserIdAndConfirmedFalse(user.getId());
		// then
		assertThat(exists).isTrue();
	}

	@DisplayName("existsByUserIdAndConfirmedFalse: 유저 ID로 읽지 않은 알림이 존재하지 않는 경우 false 를 반환한다.")
	@Test
	void givenUserId_whenNoUnreadNotifications_thenReturnFalse() {
		//given
		// when
		boolean exists = notificationRepository.existsByUserIdAndConfirmedFalse(user.getId());
		// then
		assertThat(exists).isFalse();
	}

	@DisplayName("findByCursorPagination: 커서 기반 페이지네이션으로 알림을 조회한다.")
	@Test
	void findByCursorPaginationTest() {
		//given
		for (int i = 0; i < 10; i++) {
			Notification notification = Notification.fromLike(user, review);
			entityManager.persist(notification);
		}

		NotificationSearchCondition condition = new NotificationSearchCondition(user.getId(), Sort.Direction.ASC, null, null);
		int limit = 3;

		// when
		List<Notification> notifications = notificationRepository.findByCursorPagination(condition, limit);
		boolean result = notificationRepository.findAllByUserId(user.getId())
			.stream()
			.allMatch(Notification::isConfirmed);

		// then
		assertAll(
			() -> assertThat(notifications).isNotEmpty(),
			() -> assertThat(notifications).hasSizeLessThanOrEqualTo(4),
			() -> assertThat(notifications.get(0).getUser().getId()).isEqualTo(user.getId()),
			() -> assertThat(result).isFalse()
		);
	}

	@DisplayName("일주일 이상 지난 확인된 알림만 삭제된다")
	@Test
	void shouldDeleteOnlyConfirmedNotificationsOlderThanAWeek() {
		//given
		Instant cutoffDate = Instant.now().minus(7, ChronoUnit.DAYS);
		List<Notification> deletable = notificationRepository.findAll().stream()
			.filter(notification -> notification.isConfirmed() && notification.getUpdatedAt().isBefore(cutoffDate))
			.toList();

		// when
		int deletedCount = notificationRepository.deleteOldNotificationsOlderThan(cutoffDate);
		// then
		assertThat(deletedCount).isEqualTo(deletable.size());
	}
}
