package com.codeit.sb01_deokhugam.domain.notification.exception;

import com.codeit.sb01_deokhugam.global.exception.DeokhugamException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class NotificationException extends DeokhugamException {

	public NotificationException(ErrorCode errorCode) {
		super(errorCode);
	}

	public NotificationException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
