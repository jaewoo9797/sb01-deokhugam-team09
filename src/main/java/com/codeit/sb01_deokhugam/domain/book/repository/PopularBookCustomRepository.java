package com.codeit.sb01_deokhugam.domain.book.repository;

import java.time.Instant;
import java.util.List;

import com.codeit.sb01_deokhugam.domain.book.entity.BookRanking;

public interface PopularBookCustomRepository {
	List<BookRanking> findListByCursor(String period, Instant after, String cursor, String direction, int i);

	Long getTotalElements(String period);
}
