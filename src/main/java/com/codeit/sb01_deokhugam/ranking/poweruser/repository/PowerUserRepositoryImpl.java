package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PowerUserRepositoryImpl implements PowerUserRepositoryCustom {
	@Override
	public PageResponse<PowerUserDto> findPowerUsers(GetPowerUsersRequest getPowerUsersRequest) {
		return null;
	}
}
