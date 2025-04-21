package com.codeit.sb01_deokhugam.domain.book.mapper;

import org.springframework.stereotype.Component;

import com.codeit.sb01_deokhugam.domain.book.dto.BookDto;
import com.codeit.sb01_deokhugam.domain.book.entity.Book;

@Component
public class BookMapper {
	public static BookDto toDto(Book book) {
		return new BookDto(
			book.getAuthor(), book.getCreatedAt(),
			book.getDescription(), book.getId(),
			book.getIsbn(), book.getPublishedDate(),
			book.getPublisher(), book.getRating(), book.getReviewCount(),
			book.getThumbnailUrl(), book.getTitle(), book.getUpdatedAt()
		);
	}

}
