package com.codeit.sb01_deokhugam.domain.book.controller;

import com.codeit.sb01_deokhugam.domain.book.dto.IsbnBookDto;
import com.codeit.sb01_deokhugam.domain.book.service.BookService;
import com.codeit.sb01_deokhugam.global.infra.NaverBookClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;
    private final NaverBookClient naverBookClient;

    @GetMapping("/info")
    public IsbnBookDto searchByIsbn(@RequestParam("isbn") String isbn) throws JsonProcessingException {
        IsbnBookDto isbnBookDto = naverBookClient.search(isbn);
        return isbnBookDto;
    }

}
