package com.codeit.sb01_deokhugam.domain.user.exception;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class UserAlreadyExistsException extends UserException {
	public UserAlreadyExistsException() {
		super(ErrorCode.DUPLICATION_USER);
	}

	public static UserAlreadyExistsException withEmail(String email) {
		UserAlreadyExistsException exception = new UserAlreadyExistsException();
		exception.addDetail("email", email);
		return exception;
	}

	public static UserAlreadyExistsException withNickname(String nickname) {
		UserAlreadyExistsException exception = new UserAlreadyExistsException();
		exception.addDetail("nickname", nickname);
		return exception;
	}

}
