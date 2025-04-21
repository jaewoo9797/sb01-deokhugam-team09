package com.codeit.sb01_deokhugam.domain.book.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.exception.IsbnAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.book.mapper.BookMapper;
import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private BookRepository bookRepository;
	private BookMapper bookMapper;

	public BookDto create(BookCreateRequest bookCreateRequest, String imageByte) {

		if (bookRepository.existsByIsbn(bookCreateRequest.isbn()) == true) {
			throw new IsbnAlreadyExistsException();
		}
		//given
		Book createdBook = new Book(
			"제목", "저자", "책입니다.", "12345678",
			"출판사", LocalDate.parse("2025-01-01"),
			"https://test.com",
			0, new BigDecimal("5.0"), false
		);

		return BookMapper.toDto(createdBook);
	}

}
