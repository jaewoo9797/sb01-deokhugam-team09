package com.codeit.sb01_deokhugam.domain.user.dto.request;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
	String nickname
) {
}
//닉네임 공백: DomainException/INVALID_INPUT_VALUE/닉네임은 2자 이상 20자 이하로 입력해주세요./400
//근데 "ㄱ " 이런식으로 공백들어가도 프로토타입에서 되긴됨 ;;;