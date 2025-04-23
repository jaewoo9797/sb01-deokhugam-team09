package com.codeit.sb01_deokhugam.domain.user.dto.response;

import java.time.Instant;

public record UserDto(
	String email,
	String nickname,
	Instant createdAt
) {
}
