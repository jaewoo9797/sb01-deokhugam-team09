package com.codeit.sb01_deokhugam.ranking.poweruser.service;

import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.CursorPageResponsePowerUserDto;

public interface PowerUserService {

	CursorPageResponsePowerUserDto findPowerUsers(GetPowerUsersRequest getPowerUsersRequest);

}
