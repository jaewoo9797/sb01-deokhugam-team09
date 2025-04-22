package com.codeit.sb01_deokhugam.domain.book.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.dto.BookUpdateRequest;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.exception.BookNotFoundException;
import com.codeit.sb01_deokhugam.domain.book.exception.IsbnAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.book.mapper.BookMapper;
import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;
import com.codeit.sb01_deokhugam.domain.review.service.ReviewService;
import com.codeit.sb01_deokhugam.domain.tumbnail.dto.ThumbnailDto;
import com.codeit.sb01_deokhugam.global.infra.S3StorageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;

	private final S3StorageService s3StorageService;
	private final ReviewService reviewService;

	/**
	 * 도서정보를 DB에 저장합니다.
	 *
	 * @param bookCreateRequest
	 * @param thumbnailDto
	 * @return 저장한 도서의 DTO
	 * @throws IOException
	 */
	//TODO: 이미지가 항상 어떤 타입으로 들어오는지 알아봐야함.
	@Transactional
	public BookDto create(BookCreateRequest bookCreateRequest, ThumbnailDto thumbnailDto) throws IOException {

		//isbn 중복 검증
		if (bookRepository.existsByIsbn(bookCreateRequest.isbn()) == true) {
			throw new IsbnAlreadyExistsException();
		}

		//TODO: 이미지 S3 저장 로직 필요
		//이미지 byte [] S3저장
		String imageUrl = s3StorageService.put(thumbnailDto);

		Book createdBook = new Book(
			bookCreateRequest.title(),
			bookCreateRequest.author(), bookCreateRequest.description(), bookCreateRequest.isbn(),
			bookCreateRequest.publisher(), bookCreateRequest.publishedDate(), imageUrl, 0, new BigDecimal("0.0"), false
		);

		//DB에 도서 엔티티 저장
		bookRepository.save(createdBook);

		//도서 Dto 반환
		return bookMapper.toDto(createdBook);
	}

	//도서 목록 조회

	//도서 상세 정보 조회
	public BookDto findById(UUID bookId) {
		log.debug("도서 조회 시작: id={}", bookId);
		BookDto bookDto = bookMapper.toDto(bookRepository.findById(bookId).orElseThrow(
			() -> new BookNotFoundException().withId(bookId)
		));
		log.info("도서 조회 완료: id={}", bookId);
		return bookDto;
	}

	/**
	 * 도서를 논리 삭제합니다.(soft delete)
	 *
	 * @param bookId
	 */
	//도서 논리 삭제 -soft deleted
	@Transactional
	public void delete(UUID bookId) {
		log.debug("도서 논리 삭제 시작: id={}", bookId);
		Book book = bookRepository.findById(bookId).orElseThrow(
			() -> new BookNotFoundException().withId(bookId));
		book.softDelete(); //deleted를 true로 변경
		log.info("도서 논리 삭제 완료: id={}", bookId);
	}

	/**
	 * 도서를 물리 삭제합니다. 관련된 리뷰와 댓글을 모두 삭제합니다.
	 *
	 * @param bookId
	 */
	//도서 물리 삭제
	@Transactional
	public void deletePhysical(UUID bookId) {
		log.debug("도서 물리 삭제 시작: id={}", bookId);
		Book book = bookRepository.findById(bookId).orElseThrow(
			() -> new BookNotFoundException().withId(bookId));
		log.debug("도서의 리뷰 삭제 시작: id={}", bookId);
		reviewService.deleteByBookPhysicalDelete(bookId);
		log.info("도서 물리 삭제 완료: id={}", bookId);
	}

	/**
	 * 도서 정보를 수정합니다.
	 *
	 * @param bookId
	 * @param bookUpdateRequest
	 * @param thumbnailDto
	 * @return 수정된 도서 DTO
	 * @throws IOException
	 */
	//도서 정보 수정
	@Transactional
	public BookDto update(UUID bookId, BookUpdateRequest bookUpdateRequest, ThumbnailDto thumbnailDto) throws
		IOException {

		// 기존 도서 조회
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new BookNotFoundException());

		// 이미지가 새로 들어온 경우에만 S3 업로드
		String imageUrl = book.getThumbnailUrl();
		if (thumbnailDto != null && thumbnailDto.bytes() != null) {
			imageUrl = s3StorageService.put(thumbnailDto);
		}

		// 도서 정보 업데이트
		book.update(
			bookUpdateRequest.title(),
			bookUpdateRequest.author(),
			bookUpdateRequest.description(),
			bookUpdateRequest.publisher(),
			bookUpdateRequest.publishedDate(),
			imageUrl
		);

		return bookMapper.toDto(book);
	}

	//인기 도서 목록 조회

}
