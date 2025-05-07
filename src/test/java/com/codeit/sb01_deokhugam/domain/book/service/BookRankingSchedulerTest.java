package com.codeit.sb01_deokhugam.domain.book.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.codeit.sb01_deokhugam.global.enumType.Period;

public class BookRankingSchedulerTest {

	@Mock
	private BookService bookService;

	@InjectMocks
	private BookRankingScheduler bookRankingScheduler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("도서랭킹 생성을 위한 연산을 수행한다.")
	void testUpdateRanking() {
		// when
		bookRankingScheduler.updateRanking();

		// then
		// deleteBookRanking이 1번 호출되었는지 확인한다.
		verify(bookService, times(1)).deleteBookRanking();

		// calculateRanking이 모든 Period에 대해 호출되었는지 확인한다.
		for (Period period : Period.values()) {
			verify(bookService, times(1)).calculateRanking(period);
		}
	}
}
