package com.codeit.sb01_deokhugam.auth.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

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

import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;
import com.codeit.sb01_deokhugam.util.EntityProvider;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Autowired
	private UserRepository userRepository;

	@DisplayName("유저 로그인 성공 테스트")
	@Test
	void givenValidRequest_whenLogin_thenReturnUserWith200() {
		User user = EntityProvider.createUser();
		userRepository.save(user);
		UUID userId = user.getId();
		String email = user.getEmail();
		String password = user.getPassword();
		// given
		Map<String, String> requestBody = Map.of("email", email, "password", password);
		// when & then
		given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users/login")
			.then().log().all()
			.statusCode(200)
			.body("id", equalTo(userId.toString()))
			.body("email", equalTo(email))
			.body("createdAt", notNullValue());
	}

	@DisplayName("유저 로그인 실패 테스트 - 잘못된 이메일 형식으로 로그인 요청")
	@Test
	void givenInvalidEmailFormat_whenLogin_thenReturn400() {
		User user = EntityProvider.createUser();
		userRepository.save(user);
		// given
		String invalidEmail = user.getEmail().replace("@", "");
		String password = user.getPassword();
		Map<String, String> requestBody = Map.of("email", invalidEmail, "password", password);
		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users/login")
			.then().log().all()
			.statusCode(400)
			.extract();
		// then
		JsonPath result = response.jsonPath();
		Map<String, String> details = result.getMap("details");
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(result.getString("message")).isEqualTo("요청 데이터가 유효하지 않습니다."),
			() -> assertThat(result.getString("timestamp")).isNotNull(),
			() -> assertThat(details.get("email")).isEqualTo("유효한 이메일 주소를 입력해주세요.")
		);
	}

	@DisplayName("유저 로그인 실패 테스트 - 공백으로 이루어진 문자열로 로그인 요청")
	@Test
	void givenRequestWithEmptyStringValues_whenLogin_thenReturn400() {
		// given
		String emptyString = "             ";
		Map<String, String> requestBody = Map.of("email", emptyString, "password", emptyString);
		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users/login")
			.then().log().all()
			.statusCode(400)
			.extract();
		// then
		JsonPath result = response.jsonPath();
		Map<String, String> details = result.getMap("details");
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(result.getString("message")).isEqualTo("요청 데이터가 유효하지 않습니다."),
			() -> assertThat(result.getString("timestamp")).isNotNull(),
			() -> assertThat(details.get("email")).isEqualTo("이메일은 필수 입력 항목입니다."),
			() -> assertThat(details.get("password")).isEqualTo("비밀번호는 필수 입력 항목입니다.")
		);
	}

	@DisplayName("유저 로그인 실패 테스트 - 빈 필드로 로그인 요청")
	@Test
	void givenBlankRequest_whenLogin_thenReturn400() {
		// given
		String blankField = "";
		Map<String, String> requestBody = Map.of("email", blankField, "password", blankField);
		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users/login")
			.then().log().all()
			.statusCode(400)
			.extract();
		// then
		JsonPath result = response.jsonPath();
		Map<String, String> details = result.getMap("details");
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(result.getString("message")).isEqualTo("요청 데이터가 유효하지 않습니다."),
			() -> assertThat(result.getString("timestamp")).isNotNull(),
			() -> assertThat(details.get("email")).isEqualTo("이메일은 필수 입력 항목입니다."),
			() -> assertThat(details.get("password")).isEqualTo("비밀번호는 필수 입력 항목입니다.")
		);
	}

	@DisplayName("유저 로그인 실패 테스트 - 존재하지 않는 이메일로 로그인 요청")
	@Test
	void givenNonExistentUserId_whenLogin_thenReturn401() {
		String randomString = getRandomString();
		// given
		String nonExistentEmail = randomString + "@email.com";
		String randomPassword = randomString + "123!";
		Map<String, String> requestBody = Map.of("email", nonExistentEmail, "password", randomPassword);
		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users/login")
			.then().log().all()
			.statusCode(401)
			.extract();
		// then
		JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
			() -> assertThat(result.getString("message")).isEqualTo("이메일 또는 비밀번호가 올바르지 않습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("LOGIN_INPUT_INVALID"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("유저 로그인 실패 테스트 - 이메일은 유효하나 잘못된 이메일로 로그인 요청")
	@Test
	void givenValidEmailAndInvalidPassword_whenLogin_thenReturn401() {
		User user = EntityProvider.createUser();
		// given
		String email = user.getEmail();
		String wrongPassword = getRandomString() + "123!";
		Map<String, String> requestBody = Map.of("email", email, "password", wrongPassword);
		// when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users/login")
			.then().log().all()
			.statusCode(401)
			.extract();
		// then
		JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
			() -> assertThat(result.getString("message")).isEqualTo("이메일 또는 비밀번호가 올바르지 않습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("LOGIN_INPUT_INVALID"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	private String getRandomString() {
		return UUID.randomUUID().toString().substring(0, 3);
	}
}
