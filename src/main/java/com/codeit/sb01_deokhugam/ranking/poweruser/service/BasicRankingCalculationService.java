package com.codeit.sb01_deokhugam.ranking.poweruser.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.global.schedule.utils.ScheduleUtils;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.PowerUser;
import com.codeit.sb01_deokhugam.ranking.poweruser.repository.PowerUserRankingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicRankingCalculationService implements RankingCalculationService {
	private final PowerUserRankingRepository powerUserRepository;
	private final PowerUserService powerUserService;

	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	@Transactional
	@Override
	public void calculateAllPeriodRankings() {
		//log.info("유저 랭킹 계산 시작");

		// 매일 기존 랭킹 데이터 삭제 후 기간별로 새로 계산한 데이터들을 저장한다.
		powerUserRepository.deleteAll();

		calculateRankingsForPeriod(Period.DAILY);
		calculateRankingsForPeriod(Period.WEEKLY);
		calculateRankingsForPeriod(Period.MONTHLY);
		calculateRankingsForPeriod(Period.ALL_TIME);

		//log.info("유저 랭킹 계산 성공");
	}

	private void calculateRankingsForPeriod(Period period) {
		//log.info("유저 랭킹 계산 기간: {}", period);

		// Period에 따라 계산을 시작할 범위의 시작과 끝 일자를 받아온다.
		Map.Entry<Instant, Instant> startAndEndTime = ScheduleUtils.getStartAndEndByPeriod(period);

		// DB에서 계산하여 반환한 파워유저 엔티티들을 저장한다.
		List<PowerUser> powerUsers = powerUserRepository.calculatePowerUserRank(
			startAndEndTime.getKey(), startAndEndTime.getValue(), period);
		powerUserRepository.saveAll(powerUsers);

		// 조회된 유저의 수를 저장해놓는다.
		powerUserService.updateUserCountForPeriod(period, (long)powerUsers.size());

	}

}
