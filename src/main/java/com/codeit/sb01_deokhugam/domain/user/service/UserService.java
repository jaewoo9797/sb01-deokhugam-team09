package com.codeit.sb01_deokhugam.domain.user.service;

import java.util.List;
import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;

public interface UserService {

	UserDto create(RegisterRequest userRegisterRequest);

	UserDto findActiveUser(UUID id);

	List<UserDto> findAllActiveUsers();

	UserDto findUserIncludingDeleted(UUID id);

	List<UserDto> findAllUsersIncludingDeleted();

	UserDto update(UUID pathId, UUID headerId, UserUpdateRequest userUpdateRequest);

	void softDelete(UUID id, UUID headerId);

	void hardDelete(UUID id, UUID headerId);
}
