// package com.codeit.sb01_deokhugam.domain.book.service;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.BDDMockito.*;
//
// import java.io.IOException;
// import java.math.BigDecimal;
// import java.time.Instant;
// import java.time.LocalDate;
// import java.util.Optional;
// import java.util.UUID;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.web.multipart.MultipartFile;
//
// import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
// import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
// import com.codeit.sb01_deokhugam.domain.book.entity.Book;
// import com.codeit.sb01_deokhugam.domain.book.exception.BookNotFoundException;
// import com.codeit.sb01_deokhugam.domain.book.exception.IsbnAlreadyExistsException;
// import com.codeit.sb01_deokhugam.domain.book.mapper.BookMapper;
// import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;
// import com.codeit.sb01_deokhugam.global.naver.NaverBookClient;
// import com.codeit.sb01_deokhugam.global.s3.S3Service;
//
// @ExtendWith(MockitoExtension.class)
// public class BookServiceTest {
//
// 	@Mock
// 	private BookRepository bookRepository;
//
// 	@Mock
// 	private S3Service s3SeService;
//
// 	@Mock
// 	private BookMapper bookMapper;
//
// 	@Mock
// 	private NaverBookClient naverBookClient;
//
// 	@InjectMocks
// 	private BookService bookService;
//
// 	private BookCreateRequest bookCreateRequest;
// 	private Book createdBook;
// 	private BookDto bookDto;
// 	private MultipartFile multipartFile;
// 	private Instant created;
// 	private Instant updated;
// 	private UUID bookId;
//
// 	@BeforeEach
// 	void setUp() {
//
// 		created = Instant.now();
// 		updated = Instant.now();
// 		bookId = UUID.randomUUID();
//
// 		bookCreateRequest = new BookCreateRequest(
// 			"저자",
// 			"책입니다.",
// 			"12345678",
// 			LocalDate.parse("2025-01-01"),
// 			"출판사",
// 			"제목"
// 		);
//
// 		createdBook = new Book(
// 			"제목", "저자", "책입니다.", "12345678",
// 			"출판사", LocalDate.parse("2025-01-01"),
// 			"https://test.com",
// 			0, new BigDecimal("5.0"), false
// 		);
//
// 		bookDto = new BookDto(
// 			"저자", created,
// 			"책입니다.", bookId,
// 			"12345678", LocalDate.parse("2025-01-01"), "출판사",
// 			new BigDecimal("5.0"), 0, "https://test.com", "제목", updated
// 		);
//
// 		multipartFile = Mockito.mock(MultipartFile.class);
// 	}
//
// 	@Nested
// 	@DisplayName("도서 등록 테스트")
// 	class testAddBook {
//
// 		@Test
// 		@DisplayName("도서 등록 성공")
// 		void testAddBookSuccess() throws IOException {
//
// 			//given
// 			given(bookRepository.existsByIsbn(eq(createdBook.getIsbn()))).willReturn(false);
// 			//given(s3SeService.upload(any())).willReturn("https://test.com");
// 			given(bookRepository.save(any(Book.class))).willReturn(createdBook);
// 			given(bookMapper.toDto(any(Book.class))).willReturn(bookDto);
//
// 			// when
// 			BookDto result = bookService.create(bookCreateRequest, multipartFile);
//
// 			//then
// 			//s3에 이미지 저장
// 			//리포에 북 정보 저장
// 			//verify(s3SeService).upload(any());
// 			verify(bookRepository).save(any(Book.class));
//
// 			//assertEquals(bookDto, result);
// 		}
//
// 		//중복 isbn 체크
// 		@Test
// 		@DisplayName("도서 등록 실패-중복 isbn")
// 		void testAddBookFailedCauseExistsIsbn() {
//
// 			given(bookRepository.existsByIsbn(eq(createdBook.getIsbn()))).willReturn(true);
//
// 			// when & then
// 			assertThrows(IsbnAlreadyExistsException.class, () -> {
// 				bookService.create(bookCreateRequest, multipartFile);
// 			});
//
// 			// 저장 메서드는 호출되지 않아야 한다
// 			verify(bookRepository, never()).save(any(Book.class));
// 		}
//
// 	}
// 	//삭제
//
// 	@Nested
// 	@DisplayName("도서 삭제 테스트")
// 	class testDeleteBook {
//
// 	}
//
// 	//도서 조회
//
// 	//도서 상세 정보 조회
// 	@Nested
// 	@DisplayName("도서 조회 테스트")
// 	class testFindBook {
// 		@Test
// 		@DisplayName("도서 조회 성공")
// 		void testFindBookSuccess() {
// 			//given
// 			given(bookRepository.findById(bookId)).willReturn(Optional.of(createdBook));
// 			given(bookMapper.toDto(any(Book.class))).willReturn(bookDto);
//
// 			//when
// 			BookDto result = bookService.findById(bookId);
//
// 			//then'
// 			assertEquals(bookDto, result);
//
// 		}
//
// 		@Test
// 		@DisplayName("도서 조회 실패")
// 		void testFindBookFailedCauseNotFound() {
// 			given(bookRepository.findById(bookId)).willReturn(Optional.empty());
//
// 			assertThrows(BookNotFoundException.class, () -> {
// 				bookService.findById(bookId);
// 			});
//
// 			verify(bookRepository, never()).save(any(Book.class));
//
// 		}
// 	}
//
// 	//수정
//
// 	//OCR을 통한 ISBN 정보 입력하기 (심화)
//
// 	//이미지 AWS S3 저장소 저장
//
// 	// //Naver API를 통한 ISBN 책 정보 불러오기
// 	// @Test
// 	// @DisplayName("네이버 API - ISBN 조회 테스트")
// 	// void testNaverBookApi() {
// 	//
// 	// 	//given
// 	// 	String isbn = "1234567890";
// 	// 	UUID uuid = UUID.randomUUID();
// 	// 	BookDto bookDto = new BookDto(
// 	// 		"작가",
// 	// 		Instant.now(),
// 	// 		"네이버 도서 api를 이용해서 로드한  책입니다. ",
// 	// 		uuid,
// 	// 		"1234567890",
// 	// 		LocalDate.parse("2025-01-01"),
// 	// 		"출판사",
// 	// 		new BigDecimal("5.0"),
// 	// 		0,
// 	// 		"https://www.naver.com",
// 	// 		"제목",
// 	// 		Instant.now()
// 	//
// 	// 	);
// 	// 	//
// 	// 	// //when
// 	// 	// /when(naverBookClient.)
// 	//
// 	// }
//
// }
