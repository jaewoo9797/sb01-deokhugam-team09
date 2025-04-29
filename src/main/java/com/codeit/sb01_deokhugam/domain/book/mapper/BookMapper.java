package com.codeit.sb01_deokhugam.domain.book.mapper;

import org.mapstruct.Mapper;

import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

	BookDto toDto(Book book);

	Book toBook(BookDto bookDto);

}
