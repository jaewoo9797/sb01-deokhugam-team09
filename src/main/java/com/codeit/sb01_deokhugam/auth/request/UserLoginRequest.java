package com.codeit.sb01_deokhugam.auth.request;

import com.codeit.sb01_deokhugam.domain.user.dto.ValidationSequence;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
	@NotBlank(message = "이메일은 필수 입력 항목입니다.", groups = ValidationSequence.NotBlankGroup.class)
	@Email(message = "유효한 이메일 주소를 입력해주세요.", groups = ValidationSequence.SizeAndPatternGroup.class)
	String email,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.", groups = ValidationSequence.NotBlankGroup.class)
	String password
) {
}
