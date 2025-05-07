package com.codeit.sb01_deokhugam.domain.review.exception;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class ReviewAlreadyExistsException extends ReviewException {
	public ReviewAlreadyExistsException() {
		super(ErrorCode.REVIEW_NOT_FOUND);
	}
}
