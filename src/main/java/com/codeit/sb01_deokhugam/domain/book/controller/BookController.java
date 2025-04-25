package com.codeit.sb01_deokhugam.domain.book.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.infra.NaverBookClient;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
	private final BookService bookService;
	private final NaverBookClient naverBookClient;

	/**
	 * 도서를 등록합니다.
	 * @param bookCreateRequest
	 * @param file
	 * @return 등록된 도서 정보 응댭
	 */
	@PostMapping
	public ResponseEntity<BookDto> create(@RequestPart("bookData") @Valid BookCreateRequest bookCreateRequest,
		@RequestPart(value = "thumbnailImage") MultipartFile file) {
		log.info("도서 생성 요청");
		BookDto bookDto = bookService.create(bookCreateRequest, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);
	}

	/**
	 * Naver API를 통해 ISBN으로 도서 정보를 조회합니다.
	 * @param isbn
	 * @return ISBN으로 조회된 도서 정보 응답
	 * @throws JsonProcessingException
	 */
	@GetMapping("/info")
	public ResponseEntity<IsbnBookDto> searchByIsbn(@RequestParam("isbn") String isbn) throws JsonProcessingException {

		log.info("도서 ISBN 검색 요청 : {}", isbn);
		IsbnBookDto isbnBookDto = naverBookClient.search(isbn);
		return ResponseEntity.status(HttpStatus.OK).body(isbnBookDto);
	}

	/**
	 * 검색필터링과 정렬 조건에 따라 도서 목록을 조회합니다.
	 * @param keyword
	 * @param after
	 * @param cursor
	 * @param orderBy
	 * @param direction
	 * @param limit
	 * @return 도서 목록 응답
	 */
	@GetMapping
	public ResponseEntity<PageResponse<BookDto>> findAll(
		@RequestParam(value = "keyword", required = false) String keyword,
		@RequestParam(value = "after", required = false) Instant after,
		@RequestParam(value = "cursor", required = false) String cursor,
		@RequestParam(value = "orderBy", defaultValue = "title") String orderBy,
		@RequestParam(value = "direction", defaultValue = "DESC") String direction,
		@RequestParam(value = "limit", defaultValue = "50") int limit
	) {
		log.info("도서 목록 조회 요청");
		PageResponse<BookDto> result = bookService.findAllWithCursor(keyword, after, cursor, orderBy, direction, limit);
		return ResponseEntity.ok(result);
	}

	/**
	 * 도서 하나를 조회합니다.
	 * @param bookId
	 * @return 도서 정보 응답
	 */
	@GetMapping("/{bookId}")
	public ResponseEntity<BookDto> findById(@PathVariable("bookId") UUID bookId) {
		log.info("도서 조회 요청 : {}", bookId);
		BookDto bookDto = bookService.findById(bookId);
		return ResponseEntity.status(HttpStatus.OK).body(bookDto);
	}

	/**
	 * 도서를 수정합니다.
	 * @param bookId
	 * @param bookUpdateRequest
	 * @param file
	 * @return 도서 정보 응답
	 */
	@PatchMapping("/{bookId}")
	public ResponseEntity<BookDto> update(@PathVariable("bookId") UUID bookId,
		@RequestPart("bookData") @Valid BookUpdateRequest bookUpdateRequest,
		@RequestPart(value = "thumbnailImage", required = false) MultipartFile file) {
		log.info("도서 수정 요청 : {}", bookId);
		BookDto bookDto = bookService.update(bookId, bookUpdateRequest, file);
		return ResponseEntity.status(HttpStatus.OK).body(bookDto);

	}

	/**
	 * 도서를 논리 삭제합니다.
	 * @param bookId
	 * @return
	 */
	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> delete(@PathVariable("bookId") UUID bookId) {
		log.info("도서 논리 삭제 요청 : {}", bookId);
		bookService.delete(bookId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	/**
	 * 도서를 물리 삭제합니다. 연관된 리뷰와 댓글을 함께 삭제합니다.
	 * @param bookId
	 * @return
	 */
	@DeleteMapping("/{bookId}/hard")
	public ResponseEntity<Void> deletePhysical(@PathVariable("bookId") UUID bookId) {
		log.info("도서 물리 삭제 요청 : {}", bookId);
		bookService.deletePhysical(bookId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

}
