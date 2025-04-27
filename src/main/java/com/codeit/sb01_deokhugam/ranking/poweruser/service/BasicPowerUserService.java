package com.codeit.sb01_deokhugam.ranking.poweruser.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.CursorPageResponsePowerUserDto;
import com.codeit.sb01_deokhugam.ranking.poweruser.repository.PowerUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicPowerUserService implements PowerUserService {

	PowerUserRepository powerUserRepository;

	@Transactional(readOnly = true)
	@Override
	public CursorPageResponsePowerUserDto findPowerUsers(GetPowerUsersRequest getPowerUsersRequest) {

		//db접근
		CursorPageResponsePowerUserDto powerUsers = powerUserRepository.findPowerUsers(getPowerUsersRequest);

		//적절한 반환값 형태로 변환

		return null;
	}
}
