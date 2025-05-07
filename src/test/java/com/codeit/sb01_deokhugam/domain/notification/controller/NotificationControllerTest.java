package com.codeit.sb01_deokhugam.domain.notification.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.sb01_deokhugam.domain.review.entity.Review;
import com.codeit.sb01_deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;
import com.codeit.sb01_deokhugam.util.EntityProvider;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NotificationControllerTest {

	public static final String LOGIN_USER_HEADER = "Deokhugam-Request-User-ID";
	public static final String USER_ID_UUID = "54b78d8f-68fb-4c60-8f26-00bb1a5e219d";
	public static final String NOTIFICATION_ID_UUID = "11111111-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

	@LocalServerPort
	int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@DisplayName("updateConfirm : 알림 확인 수정 API 테스트")
	@Test
	void update_notification_status_return_200() {
		// given
		Map<String, Object> requestBody = Map.of("confirmed", true);

		given().log().all()
			.header(LOGIN_USER_HEADER, USER_ID_UUID)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().patch("/api/notifications/{id}", NOTIFICATION_ID_UUID)
			.then().log().all()
			.statusCode(200)
			.body("id", equalTo(NOTIFICATION_ID_UUID))
			.body("confirmed", equalTo(true));
	}

	@DisplayName("updateConfirm : 알림 확인 수정 API 실패 테스트 - 존재하지 않는 알림 ID")
	@Test
	void update_notification_when_fail_not_exist_id_then_return_status_404() {
		//given
		UUID invalidNotificationId = UUID.randomUUID();
		Map<String, Object> requestBody = Map.of("confirmed", true);
		// when
		ExtractableResponse<Response> response = given().log().all()
			.header(LOGIN_USER_HEADER, USER_ID_UUID)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().patch("/api/notifications/{id}", invalidNotificationId)
			.then().log().all()
			.extract();
		// then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
			() -> assertThat(result.getString("message")).isEqualTo(ErrorCode.NOTIFICATION_NOT_FOUND.getMessage()),
			() -> assertThat(result.getString("code")).isEqualTo(ErrorCode.NOTIFICATION_NOT_FOUND.name()),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("PATCH /read-all 요청 시 모든 알림을 읽음 처리하고 204를 반환한다.")
	@Test
	void confirmAllNotifications_returns204NoContent() {
		//given

		// when
		ExtractableResponse<Response> response = given().log().all()
			.header(LOGIN_USER_HEADER, USER_ID_UUID)
			.contentType(ContentType.JSON)
			.when().patch("/api/notifications/read-all")
			.then().log().all()
			.extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("PATCH /read-all 요청 시 헤더에 유저 ID가 없으면 401 에러를 반환한다.")
	@Test
	void returns401_whenUserIdHeaderIsMissing() {
		//given

		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.when().patch("/api/notifications/read-all")
			.then().log().all()
			.extract();

		// then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
			() -> assertThat(result.getString("message")).isEqualTo(ErrorCode.UNAUTHORIZED.getMessage()),
			() -> assertThat(result.getString("code")).isEqualTo(ErrorCode.UNAUTHORIZED.name()),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("PATCH /read-all 요청 시 유효하지 않은 유저 ID 이면 404를 반환한다.")
	@Test
	void returnsNotFound_whenUserIdDoesNotExist() {
		//given
		UUID nonExistentUserId = UUID.randomUUID();
		// when
		ExtractableResponse<Response> response = given().log().all()
			.header(LOGIN_USER_HEADER, nonExistentUserId)
			.contentType(ContentType.JSON)
			.when().patch("/api/notifications/read-all")
			.then().log().all()
			.extract();
		// then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
			() -> assertThat(result.getString("message")).isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage()),
			() -> assertThat(result.getString("code")).isEqualTo(ErrorCode.USER_NOT_FOUND.name()),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("GET / 알림 목록 커서 기반 조회에 성공한다.")
	@Test
	void should_return_notifications_with_nextCursor_when_hasNext_is_true() {
		// given
		User user = insertNotification();
		int limit = 3;

		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.header(LOGIN_USER_HEADER, user.getId())
			.queryParam("userId", user.getId())
			.queryParam("limit", limit)
			.when().get("/api/notifications")
			.then().log().all()
			.extract();

		// then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(result.getList("content")).isNotEmpty(),
			() -> assertThat(result.getBoolean("hasNext")).isTrue(),
			() -> assertThat(result.getString("nextCursor")).isNotNull()
		);
	}

	@DisplayName("GET / 알림 개수가 limit 이하일 때 hasNext=false, nextCursor는 null이다")
	@Test
	void should_return_hasNext_false_and_null_cursor_when_notifications_do_not_exceed_limit() {
		//given
		User user = insertNotification();
		int limit = 20;

		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.header(LOGIN_USER_HEADER, user.getId())
			.queryParam("userId", user.getId())
			.queryParam("limit", limit)
			.when().get("/api/notifications")
			.then().log().all()
			.extract();

		// then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(result.getList("content")).isNotEmpty(),
			() -> assertThat(result.getBoolean("hasNext")).isFalse(),
			() -> assertThat(result.getString("nextCursor")).isNull()
		);
	}

	@DisplayName("GET / userId가 없으면 400 Bad Request 를 반환한다")
	@Test
	void should_return_400_when_userId_is_missing() {
		//given

		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.header(LOGIN_USER_HEADER, USER_ID_UUID)
			.when().get("/api/notifications")
			.then().log().all()
			.extract();

		// then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(result.getString("code")).isEqualTo("MissingServletRequestParameterException"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("GET / Header 에 userId가 없으면 401 Unauthorized 를 반환한다")
	@Test
	void should_return_401_when_userId_has_not_loginUserId() {
		//given
		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.queryParam("userId", USER_ID_UUID)
			.when().get("/api/notifications")
			.then().log().all()
			.extract();
		// then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
			() -> assertThat(result.getString("message")).isEqualTo(ErrorCode.UNAUTHORIZED.getMessage()),
			() -> assertThat(result.getString("code")).isEqualTo(ErrorCode.UNAUTHORIZED.name()),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	private User insertNotification() {
		User user = EntityProvider.createUser();
		userRepository.save(user);
		Book book = EntityProvider.createBook();
		bookRepository.save(book);
		Review review = EntityProvider.createReview(user, book);
		reviewRepository.save(review);
		List<Notification> notifications = new ArrayList<>();
		for (int i = 0; i <= 10; i++) {
			notifications.add(Notification.fromComment(user, "좋은 책입니다.", review));
		}
		notificationRepository.saveAll(notifications);
		return user;
	}
}
