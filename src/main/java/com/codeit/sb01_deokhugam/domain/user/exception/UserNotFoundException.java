package com.codeit.sb01_deokhugam.domain.user.exception;

import java.util.UUID;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class UserNotFoundException extends UserException {
	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}

	public static UserNotFoundException withId(UUID id) {
		UserNotFoundException exception = new UserNotFoundException();
		exception.addDetail("id", id);
		return exception;
	}
}
