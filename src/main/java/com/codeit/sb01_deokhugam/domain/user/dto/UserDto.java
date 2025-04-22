package com.codeit.sb01_deokhugam.domain.user.dto;

import java.time.Instant;

public record UserDto(

	String email,
	String nickname,
	String password,
	Instant createdAt
) {

}
