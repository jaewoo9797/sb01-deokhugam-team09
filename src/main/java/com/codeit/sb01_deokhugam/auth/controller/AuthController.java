package com.codeit.sb01_deokhugam.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.sb01_deokhugam.auth.request.UserLoginRequest;
import com.codeit.sb01_deokhugam.auth.service.AuthService;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/users/login")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping(path = "/")
	public ResponseEntity<UserDto> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
		log.info("로그인 요청: email={}", userLoginRequest.email());
		UserDto user = authService.login(userLoginRequest);
		log.debug("로그인 응답: {}", user);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(user);
	}
}
