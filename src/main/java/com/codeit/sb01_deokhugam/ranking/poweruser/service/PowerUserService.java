package com.codeit.sb01_deokhugam.ranking.poweruser.service;

import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;

public interface PowerUserService {

	PageResponse<PowerUserDto> findPowerUsers(GetPowerUsersRequest getPowerUsersRequest);

	void calculateAllPeriodRankings();

	void updateUserCountForPeriod(Period period, Long userNumber);
}
