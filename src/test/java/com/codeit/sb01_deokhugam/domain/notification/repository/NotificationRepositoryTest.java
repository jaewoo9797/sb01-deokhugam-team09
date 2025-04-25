package com.codeit.sb01_deokhugam.domain.notification.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.codeit.sb01_deokhugam.config.JpaAuditingConfiguration;
import com.codeit.sb01_deokhugam.config.QueryDslConfig;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.review.entity.Review;
import com.codeit.sb01_deokhugam.domain.user.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("test")
@Import({QueryDslConfig.class, JpaAuditingConfiguration.class})
class NotificationRepositoryTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NotificationRepository notificationRepository;

	private User user;
	private Review review;

	@BeforeEach
	void setUp() {
		user = new User("test@email.com", "password", "test");
		Book book = getBook();
		review = new Review(user, book, "좋은 책입니다.", new BigDecimal("4.5"));
		entityManager.persist(user);
		entityManager.persist(book);
		entityManager.persist(review);
		entityManager.flush();
		entityManager.clear();
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
		assertThat(foundNotification).isNotNull();
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
}
