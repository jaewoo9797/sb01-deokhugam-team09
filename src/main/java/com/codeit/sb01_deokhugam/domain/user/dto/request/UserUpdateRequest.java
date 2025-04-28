package com.codeit.sb01_deokhugam.domain.user.dto.request;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
	String nickname
) {
}
