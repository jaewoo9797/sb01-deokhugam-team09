package com.codeit.sb01_deokhugam.auth.exception;

import com.codeit.sb01_deokhugam.domain.user.exception.UserException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class InvalidCredentialsException extends UserException {
	public InvalidCredentialsException() {
		super(ErrorCode.LOGIN_INPUT_INVALID);
	}

	public static InvalidCredentialsException invalidIdOrPassword() {
		InvalidCredentialsException exception = new InvalidCredentialsException();
		return exception;
	}
}
