package com.codeit.sb01_deokhugam.domain.book.controller;

import java.io.IOException;
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
import com.codeit.sb01_deokhugam.domain.tumbnail.dto.ThumbnailDto;
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

	@PostMapping
	public ResponseEntity<BookDto> create(@RequestPart("bookData") @Valid BookCreateRequest bookCreateRequest,
		@RequestPart(value = "thumbnailImage", required = false) MultipartFile file) throws IOException {
		log.info("도서 생성 요청");
		//TODO: 임시로 이미지 등록x로 설정
		ThumbnailDto thumbnailDto = resolveThumbnail(file);
		BookDto bookDto = bookService.create(bookCreateRequest, thumbnailDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);
	}

	/**
	 * Naver API를 통해 ISBN으로 도서 정보를 조회합니다.
	 * @param isbn
	 * @return 도서정보 IsbnBookDto
	 * @throws JsonProcessingException
	 */
	@GetMapping("/info")
	public ResponseEntity<IsbnBookDto> searchByIsbn(@RequestParam("isbn") String isbn) throws JsonProcessingException {
		log.info("도서 ISBN 검색 요청 : {}", isbn);
		IsbnBookDto isbnBookDto = naverBookClient.search(isbn);
		return ResponseEntity.status(HttpStatus.OK).body(isbnBookDto);
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<BookDto> findById(@PathVariable("bookId") UUID bookId) {
		log.info("도서 조회 요청 : {}", bookId);
		BookDto bookDto = bookService.findById(bookId);
		return ResponseEntity.status(HttpStatus.OK).body(bookDto);
	}

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

		// 커서 기반 페이지네이션을 위한 서비스 호출
		PageResponse<BookDto> result = bookService.findAllWithCursor(keyword, after, cursor, orderBy, direction, limit);

		return ResponseEntity.ok(result);
	}

	@PatchMapping("/{bookId}")
	public ResponseEntity<BookDto> update(@PathVariable("bookId") UUID bookId,
		@RequestPart("bookData") @Valid BookUpdateRequest bookUpdateRequest,
		@RequestPart(value = "thumbnailImage", required = false) MultipartFile file) throws IOException {
		log.info("도서 수정 요청 : {}", bookId);
		ThumbnailDto thumbnailDto = null;
		if (file != null) {
			thumbnailDto = resolveThumbnail(file);
		}
		BookDto bookDto = bookService.update(bookId, bookUpdateRequest, thumbnailDto);
		return ResponseEntity.status(HttpStatus.OK).body(bookDto);

	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> delete(@PathVariable("bookId") UUID bookId) {
		log.info("도서 논리 삭제 요청 : {}", bookId);
		bookService.delete(bookId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@DeleteMapping("/{bookId}/hard")
	public ResponseEntity<Void> deletePhysical(@PathVariable("bookId") UUID bookId) {
		log.info("도서 물리 삭제 요청 : {}", bookId);
		bookService.deletePhysical(bookId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
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


