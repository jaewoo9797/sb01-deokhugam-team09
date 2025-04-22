package com.codeit.sb01_deokhugam.domain.book.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.dto.BookUpdateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.IsbnBookDto;
import com.codeit.sb01_deokhugam.domain.book.service.BookService;
import com.codeit.sb01_deokhugam.domain.tumbnail.dto.ThumbnailDto;
import com.codeit.sb01_deokhugam.global.infra.NaverBookClient;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {
	private final BookService bookService;
	private final NaverBookClient naverBookClient;

	@PostMapping
	public BookDto create(@RequestPart("bookData") @Valid BookCreateRequest bookCreateRequest,
		@RequestPart(value = "thumbnailImage", required = true) MultipartFile file) throws IOException {
		log.info("도서 생성 요청");
		ThumbnailDto thumbnailDto = resolveThumbnail(file);
		BookDto bookDto = bookService.create(bookCreateRequest, thumbnailDto);
		return bookDto;
	}

	/**
	 * Naver API를 통해 ISBN으로 도서 정보를 조회합니다.
	 * @param isbn
	 * @return 도서정보 IsbnBookDto
	 * @throws JsonProcessingException
	 */
	@GetMapping("/info")
	public IsbnBookDto searchByIsbn(@RequestParam("isbn") String isbn) throws JsonProcessingException {
		log.info("도서 ISBN 검색 요청 : {}", isbn);
		IsbnBookDto isbnBookDto = naverBookClient.search(isbn);
		return isbnBookDto;
	}

	@PatchMapping("/{bookId}")
	public BookDto update(@PathVariable("bookId") UUID bookId,
		@RequestPart("bookData") BookUpdateRequest bookUpdateRequest,
		@RequestPart(value = "thumbnailImage", required = false) MultipartFile file) throws IOException {
		log.info("도서 수정 요청 : {}", bookId);
		ThumbnailDto thumbnailDto = null;
		if (file != null) {
			thumbnailDto = resolveThumbnail(file);
		}
		BookDto bookDto = bookService.update(bookId, bookUpdateRequest, thumbnailDto);
		return bookDto;

	}

	private ThumbnailDto resolveThumbnail(MultipartFile file) {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("파일이 첨부되지 않았습니다.");
		} else {
			try {
				return new ThumbnailDto(
					UUID.randomUUID(),
					file.getBytes()
				);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}


