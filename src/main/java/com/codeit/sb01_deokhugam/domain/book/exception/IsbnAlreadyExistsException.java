package com.codeit.sb01_deokhugam.domain.book.exception;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class IsbnAlreadyExistsException extends BookException {
	public IsbnAlreadyExistsException() {
		super(ErrorCode.DUPLICATE_ISBN);
	}
}
