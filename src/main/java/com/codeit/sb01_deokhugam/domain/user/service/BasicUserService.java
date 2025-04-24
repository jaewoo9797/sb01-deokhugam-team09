package com.codeit.sb01_deokhugam.domain.user.service;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserLoginRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.exception.UserAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.user.exception.UserNotFoundException;
import com.codeit.sb01_deokhugam.domain.user.mapper.UserMapper;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

	private final UserRepository userRepository;
	private UserMapper userMapper;

	@Transactional
	@Override
	public UserDto create(RegisterRequest userRegisterRequest) {
		log.debug("사용자 생성 시작: request={}", userRegisterRequest);

		String email = userRegisterRequest.email();
		String nickname = userRegisterRequest.nickname();
		String password = userRegisterRequest.password();

		if (userRepository.existsByEmail(email)) {
			throw UserAlreadyExistsException.withEmail(email);
		}
		if (userRepository.existsByNickname(nickname)) {
			throw UserAlreadyExistsException.withNickname(nickname);
		}

		User user = new User(email, password, nickname);

		userRepository.save(user);
		log.info("{} 사용자 생성 완료: id={}, email={}", user.getNickname(), user.getId(), user.getEmail());
		return userMapper.toDto(user);
	}

	//논리 삭제되지 않은 유저 단건조회
	@Override
	public UserDto findActiveUser(UUID id) {
		log.debug("사용자 조회 시작: id={}", id);

		UserDto userDto = userRepository.findByIdAndIsDeletedFalse(id)
			.map(userMapper::toDto)
			.orElseThrow(() -> UserNotFoundException.withId(id));
		log.info("사용자 조회 완료: id={}", id);
		return userDto;
	}

	//논리 삭제되지 않은 유저 전체조회
	@Override
	public List<UserDto> findAllActiveUsers() {
		log.debug("전체 사용자 조회 시작");

		List<UserDto> userDtos = userRepository.findAllByIsDeletedFalse()
			.stream()
			.map(userMapper::toDto)
			.toList();

		log.info("전체 사용자 조회 완료: 총 {}명", userDtos.size());
		return userDtos;
	}

	//논리삭제된 유저 포함하여 단건조회
	@Override
	public UserDto findUserIncludingDeleted(UUID id) {
		log.debug("논리삭제 상태 포함하여 사용자 조회 시작: id={}", id);

		UserDto userDto = userRepository.findById(id)
			.map(userMapper::toDto)
			.orElseThrow(() -> UserNotFoundException.withId(id));

		log.info("사용자 조회 완료: id={}", id);
		return userDto;
	}

	//논리삭제된 유저 포함하여 전체조회
	@Override
	public List<UserDto> findAllUsersIncludingDeleted() {
		log.debug("논리삭제 상태 포함하여 전체 사용자 조회 시작");

		List<UserDto> userDtos = userRepository.findAll()
			.stream()
			.map(userMapper::toDto)
			.toList();

		log.info("전체 사용자 조회 완료: 총 {}명", userDtos.size());
		return userDtos;
	}

	//todo 파워유저 조회기능 추가
	@Override
	public List<PowerUserDto> findPowerUsers(Period period, String cursor, Instant after, Pageable pageable) {
		return List.of();
	}

	//
	@Override
	public UserDto update(UUID id, UserUpdateRequest userUpdateRequest) {
		log.debug("사용자 닉네임 변경 시작: id={}, request={}", id, userUpdateRequest);

		User user = userRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> UserNotFoundException.withId(id));

		String newNickname = userUpdateRequest.nickname();
		if (userRepository.existsByNickname(newNickname)) {
			throw UserAlreadyExistsException.withNickname(newNickname);
		}
		user.update(newNickname);

		log.info("사용자 닉네임 수정 완료: id={}, nickname={}", id, user.getNickname());
		return userMapper.toDto(user);
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
//해당유저 없으면 UserException/USER_NOT_FOUND/사용자를 찾을 수 없습니다./404
//논리삭제만하고 논리삭제 한번더했을때도 마찬가지로 유저낫파운드

//todo 물리삭제하면 이상한거뜸;; 물리삭제 안되는듯? 왜이럼? 나중에 확인해보기
//논리삭제 및 물리삭제 시도후실패 이후 동일이메일로 계정생성 시도시 이미존재하는 이메일이라고뜸.
/*
{
	"timestamp": "2025-04-22T06:40:26.404564816Z",
	"code": "DataIntegrityViolationException",
	"message": "could not execute statement [ERROR: update or delete on table \"users\" violates foreign key constraint \"fk8omq0tc18jd43bu5tjh6jvraq\" on table \"comments\"\n  Detail: Key (id)=(76e8a0b8-d718-47af-a3d1-ec8c27f18833) is still referenced from table \"comments\".] [delete from users where id=?]; SQL [delete from users where id=?]; constraint [fk8omq0tc18jd43bu5tjh6jvraq]",
	"details": {},
	"exceptionType": "DataIntegrityViolationException",
	"status": 500
	}*/
