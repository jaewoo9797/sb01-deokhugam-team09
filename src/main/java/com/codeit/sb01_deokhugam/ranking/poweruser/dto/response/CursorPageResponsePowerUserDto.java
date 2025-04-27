package com.codeit.sb01_deokhugam.ranking.poweruser.dto.response;

import java.util.List;

public record CursorPageResponsePowerUserDto(
	List<PowerUserDto> powerUserDtoList,
	String nextCursor, //다음페이지 커서_닉네임
	String nextAfter, //마지막 요소의 생성 시간
	Integer size, //페이지 크기
	Integer totalElements,
	boolean hasNext
) {
}
