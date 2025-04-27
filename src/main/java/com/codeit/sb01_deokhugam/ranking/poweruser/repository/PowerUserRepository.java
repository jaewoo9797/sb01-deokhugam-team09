package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.CursorPageResponsePowerUserDto;

public interface PowerUserRepository {
	CursorPageResponsePowerUserDto findPowerUsers(GetPowerUsersRequest getPowerUsersRequest);
}
