package com.codeit.sb01_deokhugam.domain.user.dto.response;

import java.util.List;

public record CursorPageResponsePowerUserDto(
	// todo 페이저블 반환값 형태로 받게될지에 따라서 수정한번더
	List<PowerUserDto> powerUserDtoList,
	String nextCursor, //다음페이지 커서_닉네임
	String nextAfter, //마지막 요소의 생성 시간
	Integer size, //페이지 크기
	Integer totalElements,
	boolean hasNext
) {
}
