package com.codeit.sb01_deokhugam.domain.book.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.dto.BookUpdateRequest;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.exception.BookNotFoundException;
import com.codeit.sb01_deokhugam.domain.book.exception.IsbnAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.book.mapper.BookMapper;
import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;
import com.codeit.sb01_deokhugam.domain.book.repository.PopularBookRepository;
import com.codeit.sb01_deokhugam.global.naver.NaverBookClient;
import com.codeit.sb01_deokhugam.global.s3.S3Service;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private PopularBookRepository popularBookRepository;

	@Mock
	private S3Service s3SeService;

	@Mock
	private BookMapper bookMapper;

	@Mock
	private NaverBookClient naverBookClient;

	@InjectMocks
	private BookService bookService;

	private BookCreateRequest bookCreateRequest;
	private Book createdBook;
	private Book mockBook;//Mock으로 만든 Book 객체, verify에서 사용된다.
	private BookDto bookDto;
	private MultipartFile multipartFile;
	private Instant created;
	private Instant updated;
	private UUID bookId;
	private UUID mockBookId;

	@BeforeEach
	void setUp() {

		created = Instant.now();
		updated = Instant.now();
		bookId = UUID.randomUUID();
		mockBookId = UUID.randomUUID();
		mockBook = Mockito.mock(Book.class);

		bookCreateRequest = new BookCreateRequest(
			"저자",
			"책입니다.",
			"12345678",
			LocalDate.parse("2025-01-01"),
			"출판사",
			"제목"
		);

		createdBook = new Book(
			"제목", "저자", "책입니다.", "12345678",
			"출판사", LocalDate.parse("2025-01-01"),
			"https://test.com",
			0, new BigDecimal("5.0"), false
		);

		bookDto = new BookDto(
			"저자", created,
			"책입니다.", bookId,
			"12345678", LocalDate.parse("2025-01-01"), "출판사",
			new BigDecimal("5.0"), 0, "https://test.com", "제목", updated
		);

		multipartFile = Mockito.mock(MultipartFile.class);

		//createBook의 id를 bookId로 세팅
		ReflectionTestUtils.setField(createdBook, "id", bookId);

	}

	@Nested
	@DisplayName("도서 등록 테스트")
	class testAddBook {

		@Test
		@DisplayName("도서 등록을 성공한다.")
		void testAddBook_ReturnsBook() {

			//given
			given(bookRepository.existsByIsbn(eq(createdBook.getIsbn()))).willReturn(false);
			given(s3SeService.upload(any(MultipartFile.class), eq("QBook")))
				.willReturn("https://test.com");
			given(bookRepository.save(any(Book.class))).willReturn(createdBook);
			given(bookMapper.toDto(any(Book.class))).willReturn(bookDto);

			// when
			BookDto result = bookService.create(bookCreateRequest, multipartFile);

			//then
			//S3에 이미지가 업로드된다.
			verify(s3SeService).upload(any(MultipartFile.class), eq("QBook"));
			//DB에 도서가 저장된다.
			verify(bookRepository).save(any(Book.class));

			assertEquals(bookDto, result);
		}

		//중복 isbn 체크
		@Test
		@DisplayName("중복된 isbn 등록을 시도하면 도서등록을 실패한다.")
		void testAddBook_FailedCauseExistsIsbn() {

			given(bookRepository.existsByIsbn(eq(createdBook.getIsbn()))).willReturn(true);

			// when & then
			assertThrows(IsbnAlreadyExistsException.class, () -> {
				bookService.create(bookCreateRequest, multipartFile);
			});

			// 저장 메서드는 호출되지 않아야 한다
			verify(bookRepository, never()).save(any(Book.class));
		}

	}

	@Nested
	@DisplayName("도서 목록 조회 테스트")
	class testSearchBooks {
		private String keyword = "자바";
		private Instant after = null;
		private String cursor = null;
		private String orderBy = "title";
		private String direction = "desc";
		private int limit = 2;

		@Test
		@DisplayName("커서와 after 없이 내림차순 도서 목록 조회를 성공한다.")
		void testSearchBooks_withoutCursorAndAfter_returnsBooks() {
			//given
			List<Book> books;
		}

		@Test
		@DisplayName("유효하지 않은 정렬 기준을 입력하여 도서 목록 조회를 실패한다.")
		void testSearchBooks_withInvalidOrderBy_returnsFail() {
			//given
		}
	}

	@Nested
	@DisplayName("도서 상세 조회 테스트")
	class testFindBook {
		@Test
		@DisplayName("bookId에 대한 도서 조회를 성공한다.")
		void testFindBook_returnsBook() {
			//given
			given(bookRepository.findByIdNotLogicalDelete(bookId)).willReturn(Optional.of(createdBook));
			given(bookMapper.toDto(any(Book.class))).willReturn(bookDto);

			//when
			BookDto result = bookService.findById(bookId);

			//then'
			assertEquals(bookDto, result);

		}

		@Test
		@DisplayName("존재하지 않는 bookId에 대하여 예외를 발생한다.")
		void testFindBook_returnsException() {
			given(bookRepository.findByIdNotLogicalDelete(bookId)).willReturn(Optional.empty());

			assertThrows(BookNotFoundException.class, () -> {
				bookService.findById(bookId);
			});

			verify(bookRepository, never()).save(any(Book.class));

		}
	}

	@Nested
	@DisplayName("도서 논리 삭제 테스트")
	class testLogicalDeleteBook {
		@Test
		@DisplayName("도서를 논리적으로 삭제 성공한다.")
		void testLogicalDeleteBook_success() {
			//given
			given(bookRepository.findByIdNotLogicalDelete(mockBookId)).willReturn(Optional.of(mockBook));

			//when
			bookService.delete(mockBookId);

			//then
			//softDelete()가 호출되었는지 검증한다.
			verify(mockBook, times(1)).softDelete();
		}

		@Test
		@DisplayName("존재하지 않는 도서의 논리적 삭제 시도를 실패한다.")
		void testLogicalDeleteBook_failed() {
			//given
			given(bookRepository.findByIdNotLogicalDelete(bookId)).willReturn(Optional.empty());

			//when & then
			assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
		}

	}

	@Nested
	@DisplayName("도서 물리 삭제 테스트")
	class testPysicallDeleteBook {
		@Test
		@DisplayName("도서를 물리적으로 삭제 성공한다.")
		void testPysicalDeleteBook_success() {
			//given
			given(bookRepository.findById(bookId)).willReturn(Optional.of(createdBook));

			//when
			bookService.deletePhysical(bookId);

			//then
			verify(bookRepository).deleteById(bookId);
		}

		@Test
		@DisplayName("존재하지 않는 도서의 물리적 삭제 시도를 실패한다.")
		void testPysicalDeleteBook_failed() {
			//given
			given(bookRepository.findById(bookId)).willReturn(Optional.empty());

			//when & then
			assertThrows(BookNotFoundException.class, () -> bookService.deletePhysical(bookId));
		}

	}

	@Nested
	@DisplayName("도서 수정 테스트")
	class testUpdateBook {
		@Test
		@DisplayName("도서의 정보 수정을 성공한다.")
		void testUpdateBook_returnsBook() {
			//given
			BookUpdateRequest bookUpdateRequest = new BookUpdateRequest("새로운 제목", "새로운 작가", "새로운 설명", "새로운 출판사",
				LocalDate.parse("2025-05-05"));
			given(bookRepository.findByIdNotLogicalDelete(mockBookId)).willReturn(Optional.of(mockBook));
			given(s3SeService.upload(any(MultipartFile.class), eq("QBook")))
				.willReturn("https://test.com");

			//when
			bookService.update(mockBookId, bookUpdateRequest, multipartFile);

			//then
			verify(mockBook).update("새로운 제목", "새로운 작가", "새로운 설명", "새로운 출판사",
				LocalDate.parse("2025-05-05"), "https://test.com");

		}

		@Test
		@DisplayName("유효하지 않은 도서의 정보 수정 시도에 대해 실패한다.")
		void testUpdateBook_failed() {
			//given
			BookUpdateRequest bookUpdateRequest = new BookUpdateRequest("새로운 제목", "새로운 작가", "새로운 설명", "새로운 출판사",
				LocalDate.parse("2025-05-05"));
			given(bookRepository.findByIdNotLogicalDelete(mockBookId)).willReturn(Optional.empty());

			//when&then
			assertThrows(BookNotFoundException.class,
				() -> bookService.update(mockBookId, bookUpdateRequest, multipartFile));
		}
	}

	//OCR을 통한 ISBN 정보 입력하기 (심화)
	@Nested
	@DisplayName("이미지를 OCR로 읽어들여 isbn을 반환한다.")
	class testExtractTextByOcr {
		@Test
		@DisplayName("이미지를 OCR로 읽어들여 isbn을 반환을 성공한다.")
		void testExtractTextByOcr_returnsIsbn() {
			//given

			//when

			//then
		}
	}

	@Nested
	@DisplayName("도서 배치 연산을 수행하여 인기 도서 랭킹을 생성한다.")
	class testCalculateBookRanking {
		@Test
		@DisplayName("DAILY 인기 도서 배치 연산을 성공하고 저장한다.")
		void testCalculateBookRanking() {

		}
	}

	@Nested
	@DisplayName("도서 랭킹 삭제")
	class testDeleteBookRanking {

		@Test
		@DisplayName("도서 랭킹 삭제를 성공한다.")
		public void testDeleteBookRanking_returnsEmpty() {
			// given
			willDoNothing().given(popularBookRepository).deleteAll();

			//when
			bookService.deleteBookRanking();

			//then
			verify(popularBookRepository, times(1)).deleteAll();
		}
	}

	// TODO: global로 빼기
	// //Naver API를 통한 ISBN 책 정보 불러오기
	// @Test
	// @DisplayName("네이버 API - ISBN 조회 테스트")
	// void testNaverBookApi() {
	//
	// 	//given
	// 	String isbn = "1234567890";
	// 	UUID uuid = UUID.randomUUID();
	// 	BookDto bookDto = new BookDto(
	// 		"작가",
	// 		Instant.now(),
	// 		"네이버 도서 api를 이용해서 로드한  책입니다. ",
	// 		uuid,
	// 		"1234567890",
	// 		LocalDate.parse("2025-01-01"),
	// 		"출판사",
	// 		new BigDecimal("5.0"),
	// 		0,
	// 		"https://www.naver.com",
	// 		"제목",
	// 		Instant.now()
	//
	// 	);
	// 	//
	// 	// //when
	// 	// /when(naverBookClient.)
	//
	// }

}
