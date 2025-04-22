package com.codeit.sb01_deokhugam.domain.tumbnail.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

//이미지 정보. 굳이싶긴한데 명시적으로 표현하기 위한 목적으로 만들었음.
public record ThumbnailDto(
	UUID id, //프로토타입에서 파일 이름을 UUID로 관리하고 있어서 id를 파일이름으로 설정할까합니다
	@NotNull(message = "파일 데이터는 필수입니다")
	byte[] bytes) {

}
