package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;

public interface PowerUserCalculationRepositoryCustom {
	List<PowerUserDto> findUserActivityBetween(Map.Entry<Instant, Instant> StartAndEndTime);
}
