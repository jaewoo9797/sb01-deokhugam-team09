package com.codeit.sb01_deokhugam.domain.user.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

	private static final String TEST_EMAIL = "user@email.net";
	private static final String TEST_NICKNAME = "김유저";
	private static final String TEST_PASSWORD = "password365!!";

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@DisplayName("유저 생성 테스트")
	@Test
	void create_user_return_status_201() {
		//given
		Map<String, Object> requestBody = Map.of(
			"email", TEST_EMAIL,
			"nickname", TEST_NICKNAME,
			"password", TEST_PASSWORD
		);

		given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users")
			.then().log().all()
			.statusCode(201)
			.body("id", notNullValue())
			.body("email", equalTo(TEST_EMAIL))
			.body("nickname", equalTo(TEST_NICKNAME))
			.body("createdAt", notNullValue());
	}

	@DisplayName("유저 생성 실패 테스트 - 잘못된 이메일, 닉네임, 비밀번호 형식")
	@Test
	void create_user_when_fail_invalid_formated_fields_then_return_status_400() {
		//given
		Map<String, Object> requestBody = Map.of(
			"email", "invalid_formated_email", //이메일 형식이 아님
			"nickname", "n", //닉네임 2자 미만
			"password", "password" //비밀번호 형식 미충족(숫자, 특수문자 없음음)
		);
		//when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users")
			.then().log().all()
			.statusCode(400)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		Map<String, String> details = result.getMap("details");
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(result.getString("message")).isEqualTo("요청 데이터가 유효하지 않습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("VALIDATION_ERROR"),
			() -> assertThat(result.getString("timestamp")).isNotNull(),

			() -> assertThat(details).hasSize(3),
			() -> assertThat(details.get("email")).isEqualTo("유효한 이메일 형식이 아닙니다."),
			() -> assertThat(details.get("nickname")).isEqualTo("닉네임은 2자 이상 20자 이하로 입력해주세요."),
			() -> assertThat(details.get("password")).isEqualTo("비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자를 포함해야 합니다.")
		);
	}

	@DisplayName("유저 생성 실패 테스트 - 공백으로 이루어진 문자열로 회원가입")
	@Test
	void create_user_when_fail_empty_string_fields_then_return_status_400() {
		//given
		Map<String, Object> requestBody = Map.of(
			"email", "            ",
			"nickname", "            ",
			"password", "            "
		);
		//when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users")
			.then().log().all()
			.statusCode(400)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		Map<String, String> details = result.getMap("details");
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(result.getString("message")).isEqualTo("요청 데이터가 유효하지 않습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("VALIDATION_ERROR"),
			() -> assertThat(result.getString("timestamp")).isNotNull(),

			() -> assertThat(details).hasSize(3),
			() -> assertThat(details.get("email")).isEqualTo("이메일은 필수 입력 항목입니다."),
			() -> assertThat(details.get("nickname")).isEqualTo("닉네임은 최소 2자 이상이어야 합니다."),
			() -> assertThat(details.get("password")).isEqualTo("비밀번호는 필수 입력 항목입니다.")
		);
	}

	@DisplayName("유저 생성 실패 테스트 - 빈 필드로 회원가입 요청")
	@Test
	void create_user_when_fail_empty_fields_then_return_status_400() {
		//given
		Map<String, Object> requestBody = Map.of(
			"email", "",
			"nickname", "",
			"password", ""
		);
		//when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users")
			.then().log().all()
			.statusCode(400)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		Map<String, String> details = result.getMap("details");
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(result.getString("message")).isEqualTo("요청 데이터가 유효하지 않습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("VALIDATION_ERROR"),
			() -> assertThat(result.getString("timestamp")).isNotNull(),

			() -> assertThat(details).hasSize(3),
			() -> assertThat(details.get("email")).isEqualTo("이메일은 필수 입력 항목입니다."),
			() -> assertThat(details.get("nickname")).isEqualTo("닉네임은 최소 2자 이상이어야 합니다."),
			() -> assertThat(details.get("password")).isEqualTo("비밀번호는 필수 입력 항목입니다.")
		);
	}

	@DisplayName("유저 조회 테스트")
	@Test
	void findUser() {

	}

	@DisplayName("유저 정보 수정 테스트")
	@Test
	void update() {

	}

	@DisplayName("유저 소프트 삭제 테스트")
	@Test
	void softDelete() {
	}

	@DisplayName("유저 하드 삭제 테스트")
	@Test
	void hardDelete() {
	}
}
