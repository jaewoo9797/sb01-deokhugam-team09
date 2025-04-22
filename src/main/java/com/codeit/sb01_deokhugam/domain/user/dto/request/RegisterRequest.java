package com.codeit.sb01_deokhugam.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 형식이 아닙니다.") //INVALID_INPUT_VALUE(잘못된 입력값입니다.) 반환
	String email,

	@NotBlank(message = "닉네임은 최소 2자 이상이어야 합니다.")
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.") //닉네임 20자 초과시
	String nickname,

	//비밀번호 조건 안지켰을 시 :  INVALID_INPUT_VALUE(잘못된 입력값입니다.) 반환
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자를 포함해야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "유효한 이메일 형식이 아닙니다.")
	String password
) {
}

//비밀번호 확인 불일치: 비밀번호가 일치하지 않습니다

//닉네임 글자수 조건, 이메일형식, 비번형식 불만족: DomainException/INVALID_INPUT_VALUE/잘못된 입력값입니다./400
//이미 존재하는 이메일로 가입 시도: UserException/EMAIL_DUPLICATION/이미 존재하는 이메일입니다./409/이메일 반환
