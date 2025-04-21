package com.codeit.sb01_deokhugam.global.infra;

import com.codeit.sb01_deokhugam.domain.book.dto.IsbnBookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NaverBookClient {

    /**
     * 네이버 도서 API를 통해 isbn으로 도서 정보를 검색합니다.
     *
     * @param isbn
     * @return 도서정보
     */
    public IsbnBookDto getBookByIsbn(String isbn) {

        //IsbnBookDto
    }
}
