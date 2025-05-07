package com.codeit.sb01_deokhugam.domain.review.exception;

import com.codeit.sb01_deokhugam.global.exception.DeokhugamException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class ReviewException extends DeokhugamException {
	public ReviewException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ReviewException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}

}
