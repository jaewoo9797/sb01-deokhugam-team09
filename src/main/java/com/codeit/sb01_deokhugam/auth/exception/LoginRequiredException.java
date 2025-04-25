package com.codeit.sb01_deokhugam.auth.exception;

import com.codeit.sb01_deokhugam.global.exception.DeokhugamException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class LoginRequiredException extends DeokhugamException {

	public LoginRequiredException(ErrorCode errorCode) {
		super(errorCode);
	}

	public LoginRequiredException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
