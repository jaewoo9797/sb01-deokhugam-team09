package com.codeit.sb01_deokhugam.domain.user.controller;

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
public class UserControllerTest {

	private static final String TEST_EMAIL = "user@email.net";
	private static final String TEST_NICKNAME = "김유저";
	private static final String TEST_PASSWORD = "password365!!";

	@LocalServerPort
	int port;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@DisplayName("유저 생성 성공 테스트")
	@Test
	void create_user_return_status_201() {
		//given
		Map<String, Object> requestBody = Map.of(
			"email", TEST_EMAIL,
			"nickname", TEST_NICKNAME,
			"password", TEST_PASSWORD
		);
		//when & then
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

	@DisplayName("유저 생성 실패 테스트 - 이미 존재하는 이메일로 회원가입 요청")
	@Test
	void create_user_when_fail_existent_email_then_return_status_409() {
		User user = insertTestUser();
		//given
		String existingEmail = user.getEmail();
		Map<String, String> requestBody = Map.of("email", existingEmail, "nickname", "user", "password",
			"password486!!");
		//when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.when().post("/api/users")
			.then().log().all()
			.statusCode(409)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value()),
			() -> assertThat(result.getString("message")).isEqualTo("이미 존재하는 사용자입니다."),
			() -> assertThat(result.getString("code")).isEqualTo("DUPLICATION_USER"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("유저 조회 성공 테스트")
	@Test
	void find_user_return_status_200() {
		User user = insertTestUser();
		//given
		UUID userId = user.getId();
		//when & then
		given().log().all()
			.when().get("/api/users/" + userId.toString())
			.then().log().all()
			.statusCode(200)
			.body("id", equalTo(userId.toString()))
			.body("email", equalTo(user.getEmail()))
			.body("nickname", equalTo(user.getNickname()))
			.body("createdAt", notNullValue());
	}

	@DisplayName("유저 조회 실패 테스트 - 논리삭제된 유저 조회")
	@Test
	void find_user_when_soft_deleted_user_then_return_status_404() {
		User user = insertTestUser();
		user.softDelete();
		userRepository.save(user);
		//given
		UUID softDeletedUserId = user.getId();
		//when
		ExtractableResponse<Response> response = given().log().all()
			.when().get("/api/users/" + softDeletedUserId.toString())
			.then().log().all()
			.statusCode(404)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
			() -> assertThat(result.getString("message")).isEqualTo("사용자를 찾을 수 없습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("USER_NOT_FOUND"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("유저 조회 실패 테스트 - 존재하지 않는 유저 조회")
	@Test
	void find_user_when_nonexistent_user_then_return_status_404() {
		//given
		UUID nonexistentUserId = UUID.randomUUID();
		//when
		ExtractableResponse<Response> response = given().log().all()
			.when().get("/api/users/" + nonexistentUserId.toString())
			.then().log().all()
			.statusCode(404)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
			() -> assertThat(result.getString("message")).isEqualTo("사용자를 찾을 수 없습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("USER_NOT_FOUND"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("유저 정보수정 성공 테스트")
	@Test
	void update_user_return_status_200() {
		User user = insertTestUser();
		//given
		Map<String, String> requestBody = Map.of("nickname", "newNickname");
		UUID userId = user.getId();
		//when & then
		given().log().all()
			.contentType(ContentType.JSON)
			.header("deokhugam-request-user-id", userId)
			.body(requestBody)
			.when().patch("/api/users/" + userId.toString())
			.then().log().all()
			.statusCode(200)
			.body("nickname", equalTo("newNickname"));
	}

	@DisplayName("유저 정보수정 실패 테스트 - 형식에 맞지 않는 닉네임으로 수정 시도")
	@Test
	void update_user_when_invalid_formated_field_then_return_status_400() {
		User user = insertTestUser();
		//given
		Map<String, String> requestBody = Map.of("nickname", "n");
		UUID userId = user.getId();
		//when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.header("deokhugam-request-user-id", userId)
			.body(requestBody)
			.when().patch("/api/users/" + userId.toString())
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
			() -> assertThat(details.get("nickname")).isEqualTo("닉네임은 2자 이상 20자 이하로 입력해주세요.")
		);
	}

	@DisplayName("유저 정보수정 실패 테스트 - 요청 헤더의 id와 경로변수 불일치")
	@Test
	void update_user_when_header_id_differs_from_path_id_then_return_status_403() {
		User user = insertTestUser();
		//given
		UUID userId = user.getId();
		UUID wrongUserId = UUID.randomUUID();
		Map<String, String> requestBody = Map.of("nickname", "newNickname");
		//when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.header("deokhugam-request-user-id", wrongUserId.toString())
			.body(requestBody)
			.when().patch("/api/users/" + userId.toString())
			.then().log().all()
			.statusCode(403)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
			() -> assertThat(result.getString("message")).isEqualTo("접근 권한이 없습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("ACCESS_DENIED"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("유저 정보수정 실패 테스트 - 존재하지 않는 유저 수정 시도")
	@Test
	void update_user_when_nonexistent_user_then_return_status_404() {
		User user = insertTestUser();
		//given
		UUID userId = user.getId();
		UUID wrongPathVariable = UUID.randomUUID();
		Map<String, String> requestBody = Map.of("nickname", "newNickname");
		//when
		ExtractableResponse<Response> response = given().log().all()
			.contentType(ContentType.JSON)
			.header("deokhugam-request-user-id", userId.toString())
			.body(requestBody)
			.when().patch("/api/users/" + wrongPathVariable.toString())
			.then().log().all()
			.statusCode(404)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
			() -> assertThat(result.getString("message")).isEqualTo("사용자를 찾을 수 없습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("USER_NOT_FOUND"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("유저 소프트 삭제 성공 테스트")
	@Test
	void soft_delete_user_return_status_204() {
		User user = insertTestUser();
		//given
		UUID userId = user.getId();

		//when
		given().log().all()
			.header("deokhugam-request-user-id", userId)
			.when().delete("/api/users/" + userId.toString())
			.then().log().all()
			.statusCode(204);

		//then
		User softDeletedUser = userRepository.findById(userId).orElseThrow();
		assertThat(softDeletedUser.isDeleted()).isTrue();
	}

	@DisplayName("유저 소프트 삭제 실패 테스트 - 요청 헤더의 id와 경로변수 불일치 ")
	@Test
	void soft_delete_user_when_header_id_differs_from_path_id_then_return_status_403() {
		User user = insertTestUser();
		//given
		UUID userId = user.getId();
		UUID wrongUserId = UUID.randomUUID();
		//when
		ExtractableResponse<Response> response = given().log().all()
			.header("deokhugam-request-user-id", wrongUserId.toString())
			.when().delete("/api/users/" + userId.toString())
			.then().log().all()
			.statusCode(403)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
			() -> assertThat(result.getString("message")).isEqualTo("접근 권한이 없습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("ACCESS_DENIED"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	@DisplayName("유저 소프트 삭제 실패 테스트 - 존재하지 않는 유저 삭제 시도")
	@Test
	void soft_delete_user_when_nonexistent_user_then_return_status_404() {
		User user = insertTestUser();
		//given
		UUID userId = user.getId();
		UUID wrongPathVariable = UUID.randomUUID();
		//when
		ExtractableResponse<Response> response = given().log().all()
			.header("deokhugam-request-user-id", userId.toString())
			.when().delete("/api/users/" + wrongPathVariable.toString())
			.then().log().all()
			.statusCode(404)
			.extract();
		//then
		final JsonPath result = response.jsonPath();
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
			() -> assertThat(result.getString("message")).isEqualTo("사용자를 찾을 수 없습니다."),
			() -> assertThat(result.getString("code")).isEqualTo("USER_NOT_FOUND"),
			() -> assertThat(result.getString("timestamp")).isNotNull()
		);
	}

	//todo 하드삭제 메서드 구현 후 테스트
	@DisplayName("유저 하드 삭제 테스트")
	@Test
	void hardDelete() {
	}

	User insertTestUser() {
		User user = EntityProvider.createUser();
		userRepository.save(user);
		return user;
	}
}

