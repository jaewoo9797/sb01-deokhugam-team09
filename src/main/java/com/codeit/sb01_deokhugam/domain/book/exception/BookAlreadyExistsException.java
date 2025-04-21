package com.codeit.sb01_deokhugam.domain.book.exception;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class BookAlreadyExistsException extends BookException {
	public BookAlreadyExistsException() {
		super(ErrorCode.DUPLICATE_BOOK);
	}
}
