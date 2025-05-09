package com.codeit.sb01_deokhugam.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codeit.sb01_deokhugam.auth.exception.AccessDeniedException;
import com.codeit.sb01_deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.sb01_deokhugam.domain.notification.repository.NotificationRepository;
import com.codeit.sb01_deokhugam.domain.review.repository.ReviewLikeRepository;
import com.codeit.sb01_deokhugam.domain.review.repository.ReviewRepository;
import com.codeit.sb01_deokhugam.domain.user.dto.request.RegisterRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.exception.UserAlreadyExistsException;
import com.codeit.sb01_deokhugam.domain.user.exception.UserNotFoundException;
import com.codeit.sb01_deokhugam.domain.user.mapper.UserMapper;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;
import com.codeit.sb01_deokhugam.util.EntityProvider;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

	@Mock
	UserMapper userMapper;
	@InjectMocks
	private BasicUserService userService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private ReviewLikeRepository reviewLikeRepository;
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private NotificationRepository notificationRepository;

	private UUID userId;
	private String email;
	private String password;
	private String nickname;
	Instant createdAt;
	private User user;
	private UserDto userDto;

	@BeforeEach
	void setUp() {
		user = EntityProvider.createUser();
		userId = UUID.randomUUID();
		email = user.getEmail();
		password = user.getPassword();
		nickname = user.getNickname();
		createdAt = user.getCreatedAt();
		userDto = new UserDto(userId, email, nickname, createdAt);
	}

	@Test
	@DisplayName("사용자 생성 성공 시 DTO 반환 ")
	void createUser_Success_ReturnDTO() {
		// given
		RegisterRequest request = new RegisterRequest(email, nickname, password);
		given(userRepository.existsByEmail(email)).willReturn(false);
		given(userMapper.toDto(any(User.class))).willReturn(userDto);

		//when
		UserDto result = userService.create(request);

		//then
		assertThat(result).isEqualTo(userDto);
		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("존재하는 이메일로 사용자 생성 시도 시 에러 반환")
	void createUser_WithExistingEmail_ThrowsException() {
		// given
		RegisterRequest request = new RegisterRequest(email, nickname, password);
		given(userRepository.existsByEmail(email)).willReturn(true);

		// when & then
		assertThatThrownBy(() -> userService.create(request)).isInstanceOf(UserAlreadyExistsException.class);
	}

	@Test
	@DisplayName("액티브유저 단건조회 _ 유저 단건 조회 성공시 해당 유저 DTO 반환")
	void findActiveUser_Success_ReturnDTO() {
		// given
		given(userRepository.findByIdAndIsDeletedFalse(userId)).willReturn(Optional.of(user));
		given(userMapper.toDto(user)).willReturn(userDto);

		// when
		UserDto result = userService.findActiveUser(userId);

		//then
		assertThat(result).isEqualTo(userDto);
		verify(userRepository).findByIdAndIsDeletedFalse(userId);
	}

	@Test
	@DisplayName("액티브유저 단건조회 _ 존재하지 않는 유저 id로 조회 시 에러 반환")
	void findActiveUser_WithNonExistentId_ThrowsException() {
		// given
		UUID nonExistentId = UUID.randomUUID();
		given(userRepository.findByIdAndIsDeletedFalse(nonExistentId)).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> userService.findActiveUser(nonExistentId)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("액티브유저 단건조회 _ 논리삭제된 유저 id로 조회 시 에러 반환")
	void findActiveUser_WithSoftDeletedUserId_ThrowsException() {
		// given
		user.softDelete();
		UUID softDeletedUserId = UUID.randomUUID();
		given(userRepository.findByIdAndIsDeletedFalse(softDeletedUserId)).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> userService.findActiveUser(softDeletedUserId)).isInstanceOf(
			UserNotFoundException.class);
	}

	@Test
	@DisplayName("액티브유저 전체조회 _ 유저 전체 조회 성공시 List<UserDTO> 반환 ")
	void findAllActiveUsers_Success_ReturnDTOList() {
		int randomListSize = RandomGenerator.getDefault().nextInt(1, 10);
		// given
		List<User> userList = userListMaker(randomListSize); // 1 이상의 랜덤한 수의 유저를 생성하여 테스트
		// 매퍼는 Mock으로 주입된 상태이기 때문에, 유저매퍼의 toDto가 호출될 때마다 이터레이터로 DTO 반환값을 하나씩 넣어준다.
		List<UserDto> expectedResult = userList.stream().map(this::userDtoMaker).toList();
		Iterator<UserDto> userDtosIterator = expectedResult.iterator();
		given(userRepository.findAllByIsDeletedFalse()).willReturn(userList);
		given(userMapper.toDto(any(User.class))).willAnswer(invocation -> userDtosIterator.next());

		// when
		List<UserDto> result = userService.findAllActiveUsers();

		// then
		assertThat(result).isEqualTo(expectedResult);
		verify(userRepository).findAllByIsDeletedFalse();
	}

	@Test
	@DisplayName("액티브유저 전체조회 _ 등록된 유저가 없어도 빈 List<UserDTO> 반환")
	void findAllActiveUsers_WithNoUsersExist_Success_ReturnEmptyList() {
		// given
		List<User> emptyUserList = Collections.emptyList();
		List<UserDto> expectedResult = emptyUserList.stream().map(this::userDtoMaker).toList();
		given(userRepository.findAllByIsDeletedFalse()).willReturn(emptyUserList);

		// when
		List<UserDto> result = userService.findAllActiveUsers();

		// then
		assertThat(result).isEqualTo(expectedResult);
		verify(userRepository).findAllByIsDeletedFalse();
	}

	@Test
	@DisplayName("액티브유저 전체조회 _ 모든 유저가 논리삭제된 상태일 때 빈 List<UserDTO> 반환")
	void findAllActiveUsers_WithSoftDeletedUsers_Success_ReturnEmptyList() {
		// given
		List<User> emptyUserList = Collections.emptyList();
		// 논리삭제된 유저는 findAllByIsDeletedFalse()로 조회되지 않기 때문에 빈 리스트 반환
		List<UserDto> expectedResult = emptyUserList.stream().map(this::userDtoMaker).toList();
		given(userRepository.findAllByIsDeletedFalse()).willReturn(emptyUserList);

		// when
		List<UserDto> result = userService.findAllActiveUsers();

		// then
		assertThat(result).isEqualTo(expectedResult);
		verify(userRepository).findAllByIsDeletedFalse();
	}

	// Dto에는 isDeleted 필드가 없어, 반환하는 userDto만 봐서는 논리삭제여부를 알 수 없음.
	@Test
	@DisplayName("모든유저(논리삭제 포함) 중 단건조회 _ 조회 성공")
	void findUserIncludingDeleted_WithSoftDeletedUser_Success_ReturnDTO() {
		user.softDelete();
		//given
		UUID softDeletedUserId = user.getId();
		given(userRepository.findById(softDeletedUserId)).willReturn(Optional.of(user));
		given(userMapper.toDto(user)).willReturn(userDto);

		//when
		UserDto result = userService.findUserIncludingDeleted(softDeletedUserId);

		//then
		assertThat(result).isEqualTo(userDto);
		verify(userRepository).findById(softDeletedUserId);

	}

	// Dto에는 isDeleted 필드가 없어, 반환하는 userDto만 봐서는 논리삭제여부를 알 수 없음.
	@Test
	@DisplayName("모든유저(논리삭제 포함) 중 단건조회 _ 존재하지 않는 유저 id로 조회 시 에러 반환")
	void findUserIncludingDeleted_WithNonExistentId_Success_ReturnDTO() {
		// given
		UUID nonExistentId = UUID.randomUUID();
		given(userRepository.findById(nonExistentId)).willReturn(Optional.of(user));

		// when, then
		assertThatThrownBy(() -> userService.findUserIncludingDeleted(nonExistentId)).isInstanceOf(
			UserNotFoundException.class);
	}

	@Test
	@DisplayName("유저 닉네임 변경 성공")
	void updateUser_Success_ReturnDTO() {
		String newName = "newTestNickname";
		// given
		UUID pathId = userId;
		UUID headerId = userId;
		UserUpdateRequest request = new UserUpdateRequest(newName);
		UserDto updatedUserDto = new UserDto(userId, email, newName, createdAt);
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.of(user));
		given(userMapper.toDto(user)).willReturn(updatedUserDto);

		// when
		UserDto result = userService.update(pathId, headerId, request);

		//then
		assertThat(result).isEqualTo(updatedUserDto);
		verify(userRepository).findByIdAndIsDeletedFalse(pathId);
	}

	@Test
	@DisplayName("존재하지 않는 유저의 닉네임 변경 시도 시 에러 반환")
	void updateUser_WithNonExistentId_ThrowsException() {
		// given
		UUID nonExistentId = UUID.randomUUID();
		UUID headerId = userId;
		UserUpdateRequest request = new UserUpdateRequest("newNickname");
		given(userRepository.findByIdAndIsDeletedFalse(nonExistentId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.update(nonExistentId, headerId, request))
			.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("다른 유저의 닉네임 변경 시도 시 에러 반환")
	void updateUser_WithDifferentUserIdInHeader_ThrowsException() {
		// given
		UUID pathId = userId;
		UUID differentHeaderId = UUID.randomUUID();
		UserUpdateRequest request = new UserUpdateRequest("newNickname");
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.of(user));

		// when & then
		assertThatThrownBy(() -> userService.update(pathId, differentHeaderId, request))
			.isInstanceOf(AccessDeniedException.class);
	}

	@Test
	@DisplayName("논리삭제된 유저의 닉네임 변경 시도 시 에러 반환")
	void updateUser_WithSoftDeletedUser_ThrowsException() {
		// given
		UUID pathId = userId;
		UUID headerId = userId;
		UserUpdateRequest request = new UserUpdateRequest("newNickname");
		user.softDelete();
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.update(pathId, headerId, request))
			.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("유저 논리삭제 성공")
	void softDelete_Success_UserCannotBeFoundAfterDeletion() {
		// given
		UUID pathId = userId;
		UUID headerId = userId;
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.of(user));

		// when
		userService.softDelete(pathId, headerId);

		// then
		assertThat(user.isDeleted()).isTrue();
		verify(userRepository).findByIdAndIsDeletedFalse(pathId);
	}

	@Test
	@DisplayName("다른 유저의 논리삭제 시도 시 에러 반환")
	void softDelete_WithDifferentUserId_ThrowsException() {
		// given
		UUID pathId = userId;
		UUID differentHeaderId = UUID.randomUUID();
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.of(user));

		// when & then
		assertThatThrownBy(() -> userService.softDelete(pathId, differentHeaderId))
			.isInstanceOf(AccessDeniedException.class);
	}

	@Test
	@DisplayName("이미 논리삭제된 유저 논리삭제 시도 시 에러 반환")
	void softDelete_WithAlreadySoftDeletedUser_ThrowsException() {
		// given
		UUID pathId = userId;
		UUID headerId = userId;
		user.softDelete();
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.softDelete(pathId, headerId))
			.isInstanceOf(UserNotFoundException.class); //todo 반환하는 예외 변경하기
	}

	@Test
	@DisplayName("존재하지 않는 유저 논리삭제 시도 시 에러 반환")
	void softDelete_WithNonExistentUserId_ThrowsException() {
		// given
		UUID pathId = UUID.randomUUID();
		UUID headerId = userId;
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.softDelete(pathId, headerId))
			.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("유저 물리삭제 성공 - 연관 데이터 모두 삭제")
	void hardDelete_Success_AllRelatedDataDeleted() {
		// given
		UUID pathId = userId;
		UUID headerId = userId;
		user = EntityProvider.createUser();
		given(userRepository.findByIdAndIsDeletedFalse(pathId)).willReturn(Optional.of(user));

		// when
		userService.hardDelete(pathId, headerId);

		// then
		verify(userRepository).findByIdAndIsDeletedFalse(pathId);
		verify(userRepository).deleteById(pathId);
		verify(reviewLikeRepository).deleteByUserId(pathId);
		verify(reviewRepository).deleteByAuthor(user);
		verify(commentRepository).deleteByUserId(pathId);
		verify(notificationRepository).deleteByUserId(pathId);
	}

	//todo 물리삭제 관련 테스트 추가

	List<User> userListMaker(int size) {
		return IntStream.range(0, size)
			.mapToObj(i -> EntityProvider.createUser())
			.toList();
	}

	UserDto userDtoMaker(User user) {
		return new UserDto(user.getId(), user.getEmail(), user.getNickname(), user.getCreatedAt());
	}
}
