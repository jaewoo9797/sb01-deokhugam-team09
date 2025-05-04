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
	@Scheduled(cron = "0 2 * * * *")
	public void updateDailyRanking() {
		bookService.calculateRanking(Period.DAILY);
		bookService.calculateRanking(Period.WEEKLY);
		bookService.calculateRanking(Period.MONTHLY);
		bookService.calculateRanking(Period.ALL_TIME);
	}
}
