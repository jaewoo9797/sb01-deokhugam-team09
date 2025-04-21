package com.codeit.sb01_deokhugam.domain.book.service;

import com.codeit.sb01_deokhugam.domain.book.dto.BookCreateRequest;
import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.exception.IsbnAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.book.mapper.BookMapper;
import com.codeit.sb01_deokhugam.domain.book.repository.BookRepository;
import com.codeit.sb01_deokhugam.global.infra.NaverBookClient;
import com.codeit.sb01_deokhugam.global.infra.S3StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BookService {

    private BookRepository bookRepository;
    private BookMapper bookMapper;

    private NaverBookClient naverBookClient;
    private S3StorageService s3StorageService;

    @Transactional
    public BookDto create(BookCreateRequest bookCreateRequest, MultipartFile image) throws IOException {

        if (bookRepository.existsByIsbn(bookCreateRequest.isbn()) == true) {
            throw new IsbnAlreadyExistsException();
        }

        /**
         * 1. isbn으로 로드한다 -> isbn 정보를 로드
         * 2. createRequest를 넣음(byte)정보
         * 3. create 메서드에서 imageUrl을 얻는다(s3에 세이브)**/

        //이미지 byte [] S3저장
        String imageUrl = s3StorageService.put(image);


        Book createdBook = new Book(
                bookCreateRequest.title(),
                bookCreateRequest.author(), bookCreateRequest.description(), bookCreateRequest.isbn(), bookCreateRequest.publisher(), bookCreateRequest.publishedDate(), imageUrl, 0, new BigDecimal("0.0"), false
        );


        //DB에 도서 엔티티 저장
        bookRepository.save(createdBook);

        //도서 Dto 반환
        return BookMapper.toDto(createdBook);
    }


}
