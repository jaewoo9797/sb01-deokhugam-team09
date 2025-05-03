package com.codeit.sb01_deokhugam.auth.exception;

import com.codeit.sb01_deokhugam.global.exception.DeokhugamException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

//옳지 않은 유저 아이디로 페이지를 접근할 때(타인 정보 수정 등) 반환하는 예외
public class AccessDeniedException extends DeokhugamException {
	public AccessDeniedException() {
		super(ErrorCode.ACCESS_DENIED);
	}

	public static AccessDeniedException accessByInvalidUser() {
		AccessDeniedException exception = new AccessDeniedException();
		return exception;
	}
}
