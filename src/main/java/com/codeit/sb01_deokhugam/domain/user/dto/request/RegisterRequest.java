package com.codeit.sb01_deokhugam.domain.user.dto.request;

import com.codeit.sb01_deokhugam.domain.user.dto.ValidationSequence;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

	@NotBlank(message = "이메일은 필수 입력 항목입니다.", groups = ValidationSequence.NotBlankGroup.class)
	@Email(message = "유효한 이메일 형식이 아닙니다.", groups = ValidationSequence.SizeAndPatternGroup.class)
	String email,

	@NotBlank(message = "닉네임은 최소 2자 이상이어야 합니다.", groups = ValidationSequence.NotBlankGroup.class)
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.", groups = ValidationSequence.SizeAndPatternGroup.class) //닉네임 20자 초과시
	String nickname,

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.", groups = ValidationSequence.NotBlankGroup.class)
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자를 포함해야 합니다.", groups = ValidationSequence.SizeAndPatternGroup.class)
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자를 포함해야 합니다.", groups = ValidationSequence.SizeAndPatternGroup.class)
	String password
) {
}
