package com.codeit.sb01_deokhugam.domain.book.exception;

import java.util.UUID;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class BookNotFoundException extends BookException {

	public BookNotFoundException() {
		super(ErrorCode.BOOK_NOT_FOUND);
	}

	public BookNotFoundException withId(UUID bookId) {
		BookNotFoundException exception = new BookNotFoundException();
		exception.addDetail("bookId", bookId);
		return exception;
	}

}
