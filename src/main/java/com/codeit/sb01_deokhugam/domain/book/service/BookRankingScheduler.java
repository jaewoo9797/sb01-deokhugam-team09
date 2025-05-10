package com.codeit.sb01_deokhugam.domain.book.service;

import static com.codeit.sb01_deokhugam.global.schedule.utils.ScheduleUtils.*;

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

	// 매일 자정 배치 연산 수행
	@Scheduled(cron = BATCH_START_TIME)
	public void updateRanking() {
		bookService.deleteBookRanking();
		//log.info("도서 랭킹 테이블 기존 데이터 삭제");

		//period에 따라 도서 랭킹 계산 시작
		for (Period period : Period.values()) {
			bookService.calculateRanking(period);
			//log.info("도서 배치 연산 수행: {} {}", Instant.now(), period.name());
		}
	}
}
