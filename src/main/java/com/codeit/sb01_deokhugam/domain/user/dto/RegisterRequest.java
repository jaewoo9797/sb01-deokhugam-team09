package com.codeit.sb01_deokhugam.domain.user.dto;

public record RegisterRequest(
	//todo 아직 조건 다 안넣었음.
	//모든 Dto는 서비스+테스트먼저 짜고 할 예정
	/*
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	String email,

	@NotBlank(message = "닉네임은 필수 입력 값입니다.")
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
	String nickname,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자를 포함해야 합니다.")
	String password*/
) {
}
