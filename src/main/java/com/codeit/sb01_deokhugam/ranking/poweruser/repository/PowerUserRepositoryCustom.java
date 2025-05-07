package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import java.util.List;

import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.PowerUser;

public interface PowerUserRepositoryCustom {
	List<PowerUser> findPowerUsers(GetPowerUsersRequest getPowerUsersRequest);
}
