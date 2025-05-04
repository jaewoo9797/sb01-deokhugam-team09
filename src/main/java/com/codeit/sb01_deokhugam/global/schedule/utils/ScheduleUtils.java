package com.codeit.sb01_deokhugam.global.schedule.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.AbstractMap;
import java.util.Map;

import com.codeit.sb01_deokhugam.global.enumType.Period;

//TODO:(고민) 의존성이 없어 static으로 사용하고 있으나, Bean으로 등록할 필요가 있을까?
public class ScheduleUtils {

	/**
	 * Period에 대해 연산 기준 시작과 끝 날자를 반환합니다. 배치 연산시 사용할 수 있습니다.
	 * @param period
	 * @return 연산 시작 날짜가 key, 끝 날짜가 value인 Map을 반환합니다.
	 */
	public static Map.Entry<Instant, Instant> getStartAndEndByPeriod(Period period) {
		LocalDate today = LocalDate.now();
		ZoneId koreaZone = ZoneOffset.UTC;

		Instant start = null;
		Instant end = null;

		switch (period) {
			case DAILY: //전날 00:00~ 전날 23:59:59.999
				start = today.minusDays(1).atStartOfDay(koreaZone).toInstant();
				end = today.minusDays(1).atTime(LocalTime.MAX).atZone(koreaZone).toInstant();
				break;
			case WEEKLY: // 7일 전 00:00 ~ 어제 23:59:59.999
				start = today.minusDays(7).atStartOfDay(koreaZone).toInstant();
				end = today.minusDays(1).atTime(LocalTime.MAX).atZone(koreaZone).toInstant();
				break;
			case MONTHLY: //한 달 전 날짜의 00:00 ~ 어제 23:59:59.999
				start = today.minusMonths(1).atStartOfDay(koreaZone).toInstant();
				end = today.minusDays(1).atTime(LocalTime.MAX).atZone(koreaZone).toInstant();
				break;
			case ALL_TIME: //2025년 1월 1일 00:00부터 어제 23:59:59.999까지.
				start = LocalDate.of(2025, 1, 1).atStartOfDay(koreaZone).toInstant();
				end = today.minusDays(1).atTime(LocalTime.MAX).atZone(koreaZone).toInstant();
				break;
		}

		return new AbstractMap.SimpleEntry<>(start, end);
	}
}
