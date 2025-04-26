package com.codeit.sb01_deokhugam.domain.user.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;

import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.CursorPageResponsePowerUserDto;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.global.enumType.Period;

public interface UserService {

	UserDto create(RegisterRequest userRegisterRequest);

	UserDto findActiveUser(UUID id);

	List<UserDto> findAllActiveUsers();

	UserDto findUserIncludingDeleted(UUID id);

	List<UserDto> findAllUsersIncludingDeleted();

	CursorPageResponsePowerUserDto findPowerUsers(Period period, Sort.Direction direction, String cursor, Instant after,
		int limit);

	UserDto update(UUID id, UserUpdateRequest userUpdateRequest);

	void softDelete(UUID id);

	void hardDelete(UUID id);
}
