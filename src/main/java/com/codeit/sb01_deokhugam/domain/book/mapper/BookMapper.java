package com.codeit.sb01_deokhugam.domain.book.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;

@Mapper(componentModel = "spring")
@Component
public interface BookMapper {

	BookDto toDto(Book book);

	Book toBook(BookDto bookDto);

}
