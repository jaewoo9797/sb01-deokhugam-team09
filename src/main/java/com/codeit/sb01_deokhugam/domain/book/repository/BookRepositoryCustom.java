package com.codeit.sb01_deokhugam.domain.book.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;

//QueryDsl 커스텀repository code를 가짐
public interface BookRepositoryCustom {
	Optional<Book> gogo(UUID id);

	List<Book> goCursor(String keyword, Instant after, String cursor, String orderBy,
		String direction, Pageable pageable);

	Long totalElements(
		@Param("keyword") String keyword
	);
}
