package com.codeit.sb01_deokhugam.domain.user.service;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserLoginRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;

public interface UserService {

	UserDto create(RegisterRequest userRegisterRequest);

	UserDto find(UUID id);

	List<UserDto> findAll();

	//커서는 사용자이름일듯. after(보조커서)는 생성일로, 컨트롤러에선 date-time 문자열로 받게됨.
	List<PowerUserDto> findPowerUsers(Period period, String cursor, Instant after, Pageable pageable);

	//닉네임만 수정 가능
	UserDto update(UUID id, UserUpdateRequest userUpdateRequest);

	// 논리 삭제. isDeleted 필드만 true로 변경
	void softDelete(UUID id);

	//물리 삭제. DB에서 데이터 삭제.
	void hardDelete(UUID id);

	UserDto login(UserLoginRequest userLoginRequest);

}

