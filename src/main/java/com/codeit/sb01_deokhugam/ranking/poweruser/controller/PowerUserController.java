package com.codeit.sb01_deokhugam.ranking.poweruser.controller;

import java.time.Instant;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.ranking.poweruser.service.PowerUserService;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users/power")
@RequiredArgsConstructor
public class PowerUserController {

	private final PowerUserService powerUserService;

	// 파워유저 목록조회
	@GetMapping
	public ResponseEntity<PageResponse> powerUser(
		// 값이 들어왔는데 변환할 타입과 맞지않을경우(ex. period에 지정한 상수 외 다른 문자 들어옴) 스프링이 400 반환)
		@RequestParam(value = "period", defaultValue = "DAILY") Period period,
		@RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction,
		@RequestParam(value = "cursor", required = false) int cursor, //조회된 페이지 마지막 등수
		@RequestParam(value = "after", required = false) Instant after, //조회된 페이지 마지막 생성일
		@RequestParam(value = "limit", defaultValue = "50") @Positive int limit) {
		GetPowerUsersRequest getPowerUsersRequest = new GetPowerUsersRequest(period, direction, cursor, after, limit);
		// todo 반환값이거아니에요!!!!!!!!!!!!! 우선 임시로 올림!!!!!!!!!!! 지금 파워유저관련기능 제정신아님주의
		PageResponse<PowerUserDto> powerUsers = powerUserService.findPowerUsers(getPowerUsersRequest);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(powerUsers);
	}
}
