package com.codeit.sb01_deokhugam.domain.book.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.codeit.sb01_deokhugam.global.enumType.Period;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookRankingScheduler {

	private final BookService bookService;

	// 매일 연산 배치 수행
	@Scheduled(cron = "2 * * * * *")
	public void updateDailyRanking() {
		//도서 랭킹 테이블을 비운다.
		bookService.deleteBookRanking();

		//period에 따라 도서 랭킹 계산 시작
		for (Period period : Period.values()) {
			bookService.calculateRanking(period);
		}
	}
}
