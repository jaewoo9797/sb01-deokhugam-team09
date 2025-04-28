package com.codeit.sb01_deokhugam.domain.user.exception;

import com.codeit.sb01_deokhugam.global.exception.DeokhugamException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class UserException extends DeokhugamException {
	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UserException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
