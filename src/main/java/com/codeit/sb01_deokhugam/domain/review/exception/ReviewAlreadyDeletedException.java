package com.codeit.sb01_deokhugam.domain.review.exception;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class ReviewAlreadyDeletedException extends ReviewException {
	public ReviewAlreadyDeletedException() {
		super(ErrorCode.REVIEW_ALREADY_DELETED);
	}
}
