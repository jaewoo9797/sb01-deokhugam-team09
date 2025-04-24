package com.codeit.sb01_deokhugam.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 주소를 입력해주세요.")
	String email,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	String password
	// 예외처리 - 타입/코드/메세지/응답코드
	//아디비번 공백들어왔을 때 DomainException/INVALID_INPUT_VALUE/잘못된 입력값입니다./400

) {
}
