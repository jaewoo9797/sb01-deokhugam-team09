package com.codeit.sb01_deokhugam.domain.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;
import com.codeit.sb01_deokhugam.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDto toDto(User user);

	List<UserDto> toDtos(List<User> users);
}
