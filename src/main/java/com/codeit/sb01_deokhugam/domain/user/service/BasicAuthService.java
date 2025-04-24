package com.codeit.sb01_deokhugam.domain.user.service;

import org.springframework.stereotype.Service;

import com.codeit.sb01_deokhugam.domain.user.dto.request.UserLoginRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.exception.InvalidCredentialsException;
import com.codeit.sb01_deokhugam.domain.user.mapper.UserMapper;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public UserDto login(UserLoginRequest userLoginRequest) {
		log.debug("로그인 시도: email={}", userLoginRequest.email());

		String email = userLoginRequest.email();
		String password = userLoginRequest.password();

		User user = userRepository.findByEmailAndIsDeletedFalse(email)
			.orElseThrow(InvalidCredentialsException::wrongPassword);

		if (!user.getPassword().equals(password)) {
			throw InvalidCredentialsException.wrongPassword();
		}

		log.debug("로그인 성공: Id={}, email={}", user.getId(), user.getEmail());
		return userMapper.toDto(user);
	}
}
