package com.codeit.sb01_deokhugam.domain.user.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.CursorPageResponsePowerUserDto;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.service.UserService;
import com.codeit.sb01_deokhugam.global.enumType.Period;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<UserDto> create(@Valid RegisterRequest registerRequest) {
		log.info("사용자 생성 요청: email={}, nickname={}", registerRequest.email(), registerRequest.nickname());
		UserDto createdUser = userService.create(registerRequest);
		log.debug("사용자 생성 응답: {}", createdUser);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(createdUser);
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<UserDto> findUser(@PathVariable UUID id) {
		UserDto user = userService.findActiveUser(id);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(user);
	}

	// 파워유저 목록조회
	@GetMapping(path = "power")
	public ResponseEntity<CursorPageResponsePowerUserDto> powerUser(
		// 값이 들어왔는데 변환할 타입과 맞지않을경우(ex. period에 지정한 상수 외 다른 문자 들어옴) 스프링이 400 반환)
		@RequestParam(value = "period", defaultValue = "DAILY") Period period,
		@RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction,
		@RequestParam(value = "cursor", required = false) String cursor, //조회된 페이지 마지막 유저 이메일
		@RequestParam(value = "after", required = false) Instant after, //조회된 페이지 마지막 생성일
		@RequestParam(value = "limit", defaultValue = "50") int limit) {
		CursorPageResponsePowerUserDto powerUsers = userService.findPowerUsers(period, direction, cursor, after, limit);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(powerUsers);
	}

	@PatchMapping(path = "{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<UserDto> update(@PathVariable UUID id, @Valid UserUpdateRequest userUpdateRequest) {
		log.info("사용자 닉네임 변경 요청: id={}, nickname={}", id, userUpdateRequest.nickname());
		UserDto updatedUser = userService.update(id, userUpdateRequest);
		log.debug("사용자 닉네임 변경 응답: {}", updatedUser);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(updatedUser);
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<Void> softDelete(@PathVariable UUID id) {
		log.info("사용자 논리 삭제 요청: id={}", id);
		userService.softDelete(id);
		log.debug("사용자 논리 삭제 성공");
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@DeleteMapping(path = "{id}/hard")
	public ResponseEntity<Void> hardDelete(@PathVariable UUID id) {
		log.info("사용자 물리 삭제 요청: id={}", id);
		userService.hardDelete(id);
		log.debug("사용자 물리 삭제 성공");
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
