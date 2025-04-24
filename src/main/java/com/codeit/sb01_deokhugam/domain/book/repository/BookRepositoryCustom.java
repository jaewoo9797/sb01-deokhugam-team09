package com.codeit.sb01_deokhugam.domain.book.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.query.Param;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;

//QueryDsl 커스텀repository code를 가짐
public interface BookRepositoryCustom {
	Optional<Book> findByIdNotLogicalDelete(UUID id);

	List<Book> findListByCursor(String keyword, Instant after, String cursor, String orderBy,
		String direction, Integer limit);

	Long getTotalElements(
		@Param("keyword") String keyword
	);
}
