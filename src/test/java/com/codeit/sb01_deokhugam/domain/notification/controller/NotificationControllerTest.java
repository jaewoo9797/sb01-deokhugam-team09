package com.codeit.sb01_deokhugam.domain.notification.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

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
	public static final String NOTIFICATION_ID_UUID = "cc1b6cbd-4b76-4204-9294-1bc918ffd1c8";

	@LocalServerPort
	int port;

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
}
