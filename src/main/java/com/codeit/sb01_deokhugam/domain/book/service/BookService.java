package com.codeit.sb01_deokhugam.domain.book.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.dto.BookUpdateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.PopularBookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.entity.BookRanking;
import com.codeit.sb01_deokhugam.domain.book.exception.BookNotFoundException;
import com.codeit.sb01_deokhugam.domain.book.exception.IsbnAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.book.mapper.BookMapper;
import com.codeit.sb01_deokhugam.domain.book.mapper.PopularBookMapper;
import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;
import com.codeit.sb01_deokhugam.domain.book.repository.PopularBookRepository;
import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.global.s3.S3Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	//image OCR
	private final Tesseract tesseract;
	private final PopularBookRepository popularBookRepository;
	private final PopularBookMapper popularBookMapper;

	//TODO: 이미지 등록 관련 로직 필요
	private final S3Service s3Service;
	//임시로 쓰던 건데 나중에 정리할게요
	//private final ReviewService reviewService;

	/**
	 * 도서정보를 DB에 저장합니다.
	 *
	 * @param bookCreateRequest
	 * @param thumnailImage
	 * @return 저장한 도서의 DTO
	 * @throws IOException
	 */
	@Transactional
	public BookDto create(BookCreateRequest bookCreateRequest, MultipartFile thumnailImage) {

		//isbn 중복 검증 - 논리적 삭제된 책 ISBN도 포함
		if (bookRepository.existsByIsbn(bookCreateRequest.isbn())) {
			throw new IsbnAlreadyExistsException().withIsbn(bookCreateRequest.isbn());
		}

		//TODO: 이미지 S3 저장 로직 필요
		//이미지 byte [] S3저장
		String imageUrl = "test.com";
		//s3Service.upload(thumnailImage, "directory");

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

	@Transactional
	//도서 목록 조회
	public PageResponse<BookDto> findAllWithCursor(String keyword, Instant after, String cursor, String orderBy,
		String direction, int limit) {

		//정렬기준 예외처리
		Set<String> validOrderBys = Set.of("title", "publishedDate", "rating", "reviewCount");
		if (!validOrderBys.contains(orderBy)) {
			throw new IllegalArgumentException("유효하지 않은 정렬 기준입니다: " + orderBy);
		}

		List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction, limit + 1);

		// 실제 size 계산 (초과 조회된 1개는 제외)
		int fetchedSize = books.size();
		boolean hasNext = fetchedSize > limit;

		// 실제로 보여줄 limit 개수만큼만 남기기
		List<Book> resultBooks = hasNext ? books.subList(0, limit) : books;
		// DTO변환
		List<BookDto> bookDtos = resultBooks.stream()
			.map(bookMapper::toDto)
			.toList();
		int size = resultBooks.size();

		//TODO: 매번 호출 비효율적.
		//totalElements 계산
		Long totalElements = bookRepository.getTotalElements(keyword);

		//nextCursor 조회. hasNext가 있으면 존재.
		String nextCursor = hasNext ? convertCursor(orderBy, books.get(size - 1)) : null;

		//nextAfter 조회
		Instant nextAfter = hasNext ? bookDtos.get(size - 1).createdAt() : null;

		return new PageResponse<>(bookDtos, nextAfter, nextCursor, size, hasNext, totalElements);
	}

	//넥스트 커서 값 반환
	private String convertCursor(String sortBy, Book book) {
		switch (sortBy) {
			case "title":
				return book.getTitle();
			case "publishedDate":
				return book.getPublishedDate().toString();
			case "rating":
				return book.getRating().toString();
			case "reviewCount":
				return book.getReviewCount().toString();
			default:
				return null;
		}

	}

	@Transactional
	//도서 상세 정보 조회
	public BookDto findById(UUID bookId) {
		log.debug("도서 조회 시작: id={}", bookId);
		BookDto bookDto = bookMapper.toDto(bookRepository.findByIdNotLogicalDelete(bookId).orElseThrow(
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
	@Transactional
	public void delete(UUID bookId) {
		log.debug("도서 논리 삭제 시작: id={}", bookId);
		Book book = bookRepository.findByIdNotLogicalDelete(bookId).orElseThrow(
			() -> new BookNotFoundException().withId(bookId));
		book.softDelete(); //엔티티의 deleted를 true로 변경
		log.info("도서 논리 삭제 완료: id={}", bookId);
	}

	/**
	 * 도서를 물리 삭제합니다. 관련된 리뷰와 댓글을 모두 삭제합니다.
	 *
	 * @param bookId
	 */
	@Transactional
	public void deletePhysical(UUID bookId) {
		log.debug("도서 물리 삭제 시작: id={}", bookId);
		bookRepository.findById(bookId).orElseThrow(
			() -> new BookNotFoundException().withId(bookId));
		log.debug("도서의 관련 리뷰 삭제 시작: id={}", bookId);
		bookRepository.deleteById(bookId);
		//TODO: 리뷰에서 물리삭제 확인 필요
		//reviewService.deleteByBookPhysicalDelete(bookId);
		log.info("도서 물리 삭제 완료: id={}", bookId);
	}

	/**
	 * 도서 정보를 수정합니다.
	 *
	 * @param bookId
	 * @param bookUpdateRequest
	 * @param thumnailImage
	 * @return 수정된 도서 DTO
	 * @throws IOException
	 */
	@Transactional
	public BookDto update(UUID bookId, BookUpdateRequest bookUpdateRequest, MultipartFile thumnailImage) {

		// 기존 도서 조회 (논리적 삭제 되지 않은 도서)
		Book book = bookRepository.findByIdNotLogicalDelete(bookId)
			.orElseThrow(() -> new BookNotFoundException().withId(bookId));

		//TODO: 이미지 로직 변경 필요

		// 이미지가 새로 들어온 경우에만 S3 업로드
		String imageUrl = book.getThumbnailUrl();
		if (thumnailImage != null) {
			imageUrl = s3Service.upload(thumnailImage, "directory");
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

	//OCR 텍스트 추출
	//TODO: 모든 예외를 도서 정보 등록 중 요류가.. 이거상속하게
	public String extractTextByOcr(MultipartFile image) throws IOException, TesseractException {

		if (image.isEmpty()) {
			throw new IllegalArgumentException("이미지가 등록되지 않았습니다");
		}

		// MultipartFile을 BufferedImage로 변환
		BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

		// OCR 수행
		String result = tesseract.doOCR(bufferedImage);

		// 숫자만 추출하기 
		String isbn = result.replaceAll("[^0-9]", "");  // 숫자 외 다른 문자 제거

		// 이미지에 ISBN이 중복되어있는 케이스 처리
		if (isbn.startsWith("97") && isbn.length() >= 13 || isbn.startsWith("98") && isbn.length() >= 13) {
			// 국제표준 ISBN 접두부 97 혹은 98로 시작하는 13자리 ISBN 번호 반환
			return isbn.substring(0, 13);
		}

		return isbn;
	}

	//인기 도서 목록 조회
	public PageResponse<PopularBookDto> findPopularBook(String period, Instant after, String cursor, String direction,
		int limit) {

		// period가 enum에 속하는지 검증 ->
		try {
			Period.valueOf(period);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("잘못된 period가 전달되었습니다. period  : " + period);
		}

		List<BookRanking> bookRankings = popularBookRepository.findListByCursor(period, after, cursor, direction,
			limit + 1);

		// 실제 size 계산 (초과 조회된 1개는 제외)
		int fetchedSize = bookRankings.size();
		boolean hasNext = fetchedSize > limit;

		// 실제로 보여줄 limit 개수만큼만 남기기
		List<BookRanking> resultBooks = hasNext ? bookRankings.subList(0, limit) : bookRankings;

		List<PopularBookDto> popularBookDtos = resultBooks.stream()
			.map(popularBookMapper::toDto)
			.toList();
		int size = resultBooks.size();

		//TODO: 매번 호출 비효율적.
		//totalElements 계산
		Long totalElements = popularBookRepository.getTotalElements(period);

		//nextCursor 조회
		String nextCursor =
			hasNext ? String.valueOf(cursor == null ? limit : Integer.parseInt(cursor) + limit) : null;

		//nextAfter 조회
		Instant nextAfter = hasNext ? popularBookDtos.get(size - 1).createdAt() : null;

		return new PageResponse<>(popularBookDtos, nextAfter, nextCursor, size, hasNext, totalElements);

	}

	//TODO: 도서 리뷰 업데이트(리뷰서비스에서 호출? )

}
