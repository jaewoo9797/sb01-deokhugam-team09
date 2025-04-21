package com.codeit.sb01_deokhugam.book;

import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.exception.IsbnAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.book.mapper.BookMapper;
import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;
import com.codeit.sb01_deokhugam.domain.book.service.BookService;
import com.codeit.sb01_deokhugam.global.infra.NaverBookClient;
import com.codeit.sb01_deokhugam.global.infra.S3StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private NaverBookClient naverBookClient;

    @InjectMocks
    private BookService bookService;

    @Nested
    @DisplayName("도서 등록 테스트")
    class testAddBook {
        Instant created = Instant.now();
        Instant updated = Instant.now();
        UUID bookId = UUID.randomUUID();

        BookCreateRequest bookCreateRequest = new BookCreateRequest(
                "저자",
                "책입니다.",
                "12345678",
                LocalDate.parse("2025-01-01"),
                "출판사",
                "제목"
        );

        Book createdBook = new Book(
                "제목", "저자", "책입니다.", "12345678",
                "출판사", LocalDate.parse("2025-01-01"),
                "https://test.com",
                0, new BigDecimal("5.0"), false
        );

        BookDto bookDto = new BookDto(
                "저자", created,
                "책입니다.", bookId,
                "12345678", LocalDate.parse("2025-01-01"), "출판사",
                new BigDecimal("5.0"), 0, "https://test.com", "제목", updated
        );

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);


        @Test
        @DisplayName("도서 등록 성공")
        void testAddBook() throws IOException {

            //given
            given(bookRepository.existsByIsbn(eq(createdBook.getIsbn()))).willReturn(false);
            given(s3StorageService.put(any())).willReturn("https://test.com");
            given(bookRepository.save(any(Book.class))).willReturn(createdBook);


            // when
            BookDto result = bookService.create(bookCreateRequest, multipartFile);

            //then
            //s3에 이미지 저장
            //리포에 북 정보 저장
            verify(s3StorageService).put(any());
            verify(bookRepository).save(any(Book.class));

            //assertEquals(bookDto, result);
        }

        @Test
        @DisplayName("도서 등록 실패-중복 isbn")
        void testAddBookFailedCauseExistsIsbn() {

            given(bookRepository.existsByIsbn(eq(createdBook.getIsbn()))).willReturn(true);

            // when & then
            assertThrows(IsbnAlreadyExistsException.class, () -> {
                bookService.create(bookCreateRequest, multipartFile);
            });

            // 저장 메서드는 호출되지 않아야 한다
            verify(bookRepository, never()).save(any(Book.class));
        }

        //isbn 중복 체크

    }

    //Naver API를 통한 ISBN 책 정보 불러오기

    //OCR을 통한 ISBN 정보 입력하기 (심화)

    //이미지 AWS S3 저장소 저장
    @Test
    @DisplayName("네이버 API - ISBN 조회 테스트")
    void testNaverBookApi() {

        //given
        String isbn = "1234567890";
        UUID uuid = UUID.randomUUID();
        BookDto bookDto = new BookDto(
                "작가",
                Instant.now(),
                "네이버 도서 api를 이용해서 로드한  책입니다. ",
                uuid,
                "1234567890",
                LocalDate.parse("2025-01-01"),
                "출판사",
                new BigDecimal("5.0"),
                0,
                "https://www.naver.com",
                "제목",
                Instant.now()

        );
        //
        // //when
        // /when(naverBookClient.)

    }

}
