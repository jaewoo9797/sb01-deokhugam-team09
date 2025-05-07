package com.codeit.sb01_deokhugam.domain.review.exception;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class ReviewAuthorityException extends ReviewException {
	public ReviewAuthorityException() {
		super(ErrorCode.NOT_AUTHORITY);
	}
}
