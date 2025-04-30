package com.codeit.sb01_deokhugam.domain.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.exception.UserAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.user.exception.UserNotFoundException;
import com.codeit.sb01_deokhugam.domain.user.mapper.UserMapper;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Transactional(readOnly = false)
	@Override
	public UserDto create(RegisterRequest userRegisterRequest) {
		log.debug("사용자 생성 시작: request={}", userRegisterRequest);

		String email = userRegisterRequest.email();
		String nickname = userRegisterRequest.nickname();
		String password = userRegisterRequest.password();

		if (userRepository.existsByEmail(email)) {
			throw UserAlreadyExistsException.withEmail(email);
		}

		User user = new User(email, password, nickname);

		userRepository.save(user);
		log.info("사용자 생성 완료: id={}, email={}, nickname={}", user.getId(), user.getEmail(), user.getNickname());
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

		List<UserDto> userDtos = userRepository.findAllByIsDeletedFalse().stream().map(userMapper::toDto).toList();

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

		List<UserDto> userDtos = userRepository.findAll().stream().map(userMapper::toDto).toList();

		log.info("전체 사용자 조회 완료: 총 {}명", userDtos.size());
		return userDtos;
	}

	//유저 닉네임 변경
	@Override
	@Transactional(readOnly = false)
	public UserDto update(UUID id, UserUpdateRequest userUpdateRequest) {
		log.debug("사용자 닉네임 변경 시작: id={}, request={}", id, userUpdateRequest);

		User user = userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> UserNotFoundException.withId(id));

		String newNickname = userUpdateRequest.nickname();
		user.update(newNickname);

		log.info("사용자 닉네임 수정 완료: id={}, nickname={}", id, user.getNickname());
		return userMapper.toDto(user);
	}

	//유저 isDeleted 필드 false로 변경
	@Override
	@Transactional(readOnly = false)
	public void softDelete(UUID id) {
		log.debug("사용자 논리삭제 시작: id={}", id);

		User user = userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> UserNotFoundException.withId(id));
		user.softDelete();

		log.info("사용자 논리삭제 완료: id={}", id);
	}

	//물리 삭제
	@Override
	@Transactional(readOnly = false)
	public void hardDelete(UUID id) {
		log.debug("사용자 물리삭제 시작: id={}", id);

		if (!userRepository.existsById(id)) {
			throw UserNotFoundException.withId(id);
		}
		userRepository.deleteById(id);

		log.info("사용자 물리삭제 완료: id={}", id);
	}
}
