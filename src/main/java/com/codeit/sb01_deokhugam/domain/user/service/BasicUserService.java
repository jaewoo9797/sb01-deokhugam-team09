package com.codeit.sb01_deokhugam.domain.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.sb01_deokhugam.auth.exception.AccessDeniedException;
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
	//Dto에는 isDeleted 필드가 없어, 반환하는 userDto만 봐서는 논리삭제여부를 알 수 없다.
	@Override
	public UserDto findUserIncludingDeleted(UUID id) {
		log.debug("논리삭제 상태 포함하여 사용자 조회 시작: id={}", id);

		UserDto userDto = userRepository.findById(id)
			.map(userMapper::toDto)
			.orElseThrow(() -> UserNotFoundException.withId(id));

		log.info("사용자 조회 완료: id={}", id);
		return userDto;
	}

	//유저 닉네임 변경
	@Override
	@Transactional
	public UserDto update(UUID pathId, UUID headerId, UserUpdateRequest userUpdateRequest) {
		log.debug("사용자 닉네임 변경 시작: pathId={}, request={}", pathId, userUpdateRequest);

		User user = userRepository.findByIdAndIsDeletedFalse(pathId).orElseThrow(() -> UserNotFoundException.withId(
			pathId));
		verifyUserMatch(pathId, headerId);

		String newNickname = userUpdateRequest.nickname();
		user.update(newNickname);

		log.info("사용자 닉네임 수정 완료: pathId={}, nickname={}", pathId, user.getNickname());
		return userMapper.toDto(user);
	}

	// todo 이미 논리삭제되어있는 유저의 경우, 다른 예외처리를 반환하는게 보기 좋을듯.
	//유저 isDeleted 필드 false로 변경
	@Override
	@Transactional
	public void softDelete(UUID pathId, UUID headerId) {
		log.debug("사용자 논리삭제 시작: id={}", pathId);

		User user = userRepository.findByIdAndIsDeletedFalse(pathId)
			.orElseThrow(() -> UserNotFoundException.withId(pathId));
		verifyUserMatch(pathId, headerId);
		user.softDelete();

		log.info("사용자 논리삭제 완료: id={}", pathId);
	}

	// todo 물리 삭제 제대로 구현
	@Override
	@Transactional
	public void hardDelete(UUID pathId, UUID headerId) {
		log.debug("사용자 물리삭제 시작: id={}", pathId);

		if (!userRepository.existsById(pathId)) {
			throw UserNotFoundException.withId(pathId);
		}
		verifyUserMatch(pathId, headerId);
		userRepository.deleteById(pathId);

		log.info("사용자 물리삭제 완료: id={}", pathId);
	}

	// 경로변수와 헤더에 기재된 id의 일치여부 비교
	private void verifyUserMatch(UUID pathId, UUID headerId) {
		if (!pathId.equals(headerId)) {
			throw AccessDeniedException.accessByInvalidUser();
		}
	}

}


