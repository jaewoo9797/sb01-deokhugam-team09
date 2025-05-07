package com.codeit.sb01_deokhugam.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codeit.sb01_deokhugam.auth.exception.InvalidCredentialsException;
import com.codeit.sb01_deokhugam.auth.request.UserLoginRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.mapper.UserMapper;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;
import com.codeit.sb01_deokhugam.util.EntityProvider;

@ExtendWith(MockitoExtension.class)
class BasicAuthServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private UserMapper userMapper;
	@InjectMocks
	private BasicAuthService authService;

	private UUID userId;
	private String email;
	private String nickname;
	private String password;
	private User user;
	private UserDto userDto;

	@BeforeEach
	void setUp() {
		user = EntityProvider.createUser();
		userId = UUID.randomUUID();
		email = user.getEmail();
		nickname = user.getNickname();
		password = user.getPassword();
		userDto = new UserDto(userId, email, user.getNickname(), user.getCreatedAt());
	}

	@Test
	@DisplayName("로그인 성공 시 UserDto 반환")
	void login_Success_ReturnUserDto() {
		// given
		UserLoginRequest request = new UserLoginRequest(email, password);
		given(userRepository.findByEmailAndIsDeletedFalse(email)).willReturn(Optional.of(user));
		given(userMapper.toDto(user)).willReturn(userDto);

		// when
		UserDto result = authService.login(request);

		// then
		assertThat(result).isEqualTo(userDto);
		verify(userRepository).findByEmailAndIsDeletedFalse(email);
		verify(userMapper).toDto(user);
	}

	@Test
	@DisplayName("존재하지 않는 이메일로 로그인 시도 시도 시 에러 반환")
	void login_WithNonExistentEmail_ThrowsException() {
		// given
		String nonExistentEmail = "nonexistent@example.com";
		UserLoginRequest request = new UserLoginRequest(nonExistentEmail, password);
		given(userRepository.findByEmailAndIsDeletedFalse(nonExistentEmail)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> authService.login(request)).isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("잘못된 비밀번호로 로그인 시도 시 에러 반환")
	void login_WithInvalidPassword_ThrowsException() {
		// given
		String invalidPassword = "wrongPassword";
		UserLoginRequest request = new UserLoginRequest(email, invalidPassword);
		given(userRepository.findByEmailAndIsDeletedFalse(email)).willReturn(Optional.of(user));

		// when & then
		assertThatThrownBy(() -> authService.login(request)).isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("논리삭제된 유저로 로그인 시도 시 에러 반환")
	void login_WithSoftDeletedUser_ThrowsException() {
		// given
		user.softDelete();
		UserLoginRequest request = new UserLoginRequest(email, password);
		given(userRepository.findByEmailAndIsDeletedFalse(email)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> authService.login(request)).isInstanceOf(InvalidCredentialsException.class);
	}
} 
