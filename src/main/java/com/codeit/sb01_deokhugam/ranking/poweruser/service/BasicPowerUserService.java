package com.codeit.sb01_deokhugam.ranking.poweruser.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.ranking.poweruser.repository.PowerUserRepositoryCustom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BasicPowerUserService implements PowerUserService {

	private final PowerUserRepositoryCustom powerUserRepositoryCustom;

	@Override
	public PageResponse<PowerUserDto> findPowerUsers(GetPowerUsersRequest getPowerUsersRequest) {

		//db접근
		PageResponse<PowerUserDto> powerUsers = powerUserRepositoryCustom.findPowerUsers(getPowerUsersRequest);

		//적절한 반환값 형태로 변환

		return null;
	}
}
