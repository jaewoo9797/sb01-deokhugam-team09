package com.codeit.sb01_deokhugam.domain.book.mapper;

import org.mapstruct.Mapper;

import com.codeit.sb01_deokhugam.domain.book.dto.PopularBookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.BookRanking;

@Mapper(componentModel = "spring")
public interface PopularBookMapper {
	PopularBookDto toDto(BookRanking bookRanking);
}
