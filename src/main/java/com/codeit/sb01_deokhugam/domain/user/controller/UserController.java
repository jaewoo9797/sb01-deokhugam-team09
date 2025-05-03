package com.codeit.sb01_deokhugam.domain.user.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.sb01_deokhugam.domain.user.dto.ValidationSequence;
import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<UserDto> create(
		@Validated(ValidationSequence.class) @RequestBody RegisterRequest registerRequest) {
		log.info("사용자 생성 요청: email={}, nickname={}", registerRequest.email(), registerRequest.nickname());
		UserDto createdUser = userService.create(registerRequest);
		log.debug("사용자 생성 응답: {}", createdUser);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(createdUser);
	}

	@GetMapping(path = "{userId}")
	public ResponseEntity<UserDto> findUser(@PathVariable UUID userId) {
		UserDto user = userService.findActiveUser(userId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(user);
	}

	// todo 헤더가 없을 시 예외처리 나중에 추가
	@PatchMapping(path = "{pathId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> update(@RequestHeader("deokhugam-request-user-id") UUID headerId,
		@PathVariable UUID pathId,
		@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
		log.info("사용자 닉네임 변경 요청: pathId={}, nickname={}", pathId, userUpdateRequest.nickname());
		UserDto updatedUser = userService.update(pathId, headerId, userUpdateRequest);
		log.debug("사용자 닉네임 변경 응답: {}", updatedUser);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(updatedUser);
	}

	@DeleteMapping(path = "{pathId}")
	public ResponseEntity<Void> softDelete(@RequestHeader("deokhugam-request-user-id") UUID headerId,
		@PathVariable UUID pathId) {
		log.info("사용자 논리 삭제 요청: pathId={}", pathId);
		userService.softDelete(pathId, headerId);
		log.debug("사용자 논리 삭제 성공");
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@DeleteMapping(path = "{id}/hard")
	public ResponseEntity<Void> hardDelete(@RequestHeader("deokhugam-request-user-id") UUID headerId,
		@PathVariable UUID pathId) {
		log.info("사용자 물리 삭제 요청: pathId={}", pathId);
		userService.hardDelete(pathId, headerId);
		log.debug("사용자 물리 삭제 성공");
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
