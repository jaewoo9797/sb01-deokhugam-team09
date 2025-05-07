package com.codeit.sb01_deokhugam.ranking.poweruser.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.global.schedule.utils.ScheduleUtils;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.PowerUser;
import com.codeit.sb01_deokhugam.ranking.poweruser.repository.PowerUserCalculationRepositoryCustom;
import com.codeit.sb01_deokhugam.ranking.poweruser.repository.PowerUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingCalculationService {

	private final PowerUserCalculationRepositoryCustom powerUserCalculationRepositoryCustom;
	private final PowerUserRepository powerUserRepository;

	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	@Transactional
	public void calculateAllPeriodRankings() {
		log.info("유저 랭킹 계산 시작");

		// 기존 랭킹 데이터 삭제
		powerUserRepository.deleteAll();

		// 각 기간별 랭킹 계산
		calculateRankingsForPeriod(Period.DAILY);
		calculateRankingsForPeriod(Period.WEEKLY);
		calculateRankingsForPeriod(Period.MONTHLY);
		calculateRankingsForPeriod(Period.ALL_TIME);

		log.info("유저 랭킹 계산 성공");
	}

	private void calculateRankingsForPeriod(Period period) {
		log.info("유저 랭킹 계산 기간: {}", period);

		// 기간에 따른 시작 시간 계산
		Map.Entry<Instant, Instant> StartAndEndTime = ScheduleUtils.getStartAndEndByPeriod(period);

		// DB에서 직접 계산된 결과 조회
		List<PowerUserDto> powerUserDtos = powerUserCalculationRepositoryCustom.findUserActivityBetween(
			StartAndEndTime);

		// 랭킹 데이터 저장
		for (int i = 0; i < powerUserDtos.size(); i++) {
			PowerUserDto dto = powerUserDtos.get(i);

			PowerUser powerUser = new PowerUser(dto.userId(), dto.nickname(), period, i + 1,
				BigDecimal.valueOf(dto.score()), BigDecimal.valueOf(dto.reviewScoreSum()), dto.likeCount(),
				dto.commentCount());

			powerUserRepository.save(powerUser);
		}
	}
} 
