package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;

public interface PowerUserRepository {
	PageResponse<PowerUserDto> findPowerUsers(GetPowerUsersRequest getPowerUsersRequest);
}
