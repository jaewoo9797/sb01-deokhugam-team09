package com.codeit.sb01_deokhugam.ranking.poweruser.mapper;

import java.time.Instant;
import java.util.List;

import org.mapstruct.Mapper;

import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.PowerUser;

@Mapper(componentModel = "spring")
public interface PowerUserMapper {
	PowerUserDto toDto(PowerUser powerUser);

	PageResponse<PowerUserDto> toPageResponseDto(List<PowerUser> powerUsers, Instant nextAfter, int nextCursor,
		int size, boolean hasNext, long totalElements);

}
