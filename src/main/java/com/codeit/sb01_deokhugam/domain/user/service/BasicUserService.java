package com.codeit.sb01_deokhugam.domain.user.service;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserLoginRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;

@Service
public class BasicUserService implements UserService {

	@Override
	public UserDto create(RegisterRequest userRegisterRequest) {
		return null;
	}

	@Override
	public UserDto find(UUID id) {
		return null;
	}

	@Override
	public List<UserDto> findAll() {
		return List.of();
	}

	@Override
	public List<PowerUserDto> findPowerUsers(Period period, String cursor, Instant after, Pageable pageable) {
		return List.of();
	}

	@Override
	public UserDto update(UUID id, UserUpdateRequest userUpdateRequest) {
		return null;
	}

	@Override
	public void softDelete(UUID id) {

	}

	@Override
	public void hardDelete(UUID id) {

	}

	@Override
	public UserDto login(UserLoginRequest userLoginRequest) {
		return null;
	}
}
