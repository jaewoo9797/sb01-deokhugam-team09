package com.codeit.sb01_deokhugam.domain.user.dto.response;

import java.time.Instant;

public record UserDto(
	String email,
	String nickname,
	String password,
	Instant createdAt
) {
}
//해당유저 없으면 UserException/USER_NOT_FOUND/사용자를 찾을 수 없습니다./404
//논리삭제만하고 논리삭제 한번더했을때도 마찬가지로 유저낫파운드

//todo 물리삭제하면 이상한거뜸;; 물리삭제 안되는듯? 왜이럼? 나중에 확인해보기
//논리삭제 및 물리삭제 시도후실패 이후 동일이메일로 계정생성 시도시 이미존재하는 이메일이라고뜸.
/*
{
	"timestamp": "2025-04-22T06:40:26.404564816Z",
	"code": "DataIntegrityViolationException",
	"message": "could not execute statement [ERROR: update or delete on table \"users\" violates foreign key constraint \"fk8omq0tc18jd43bu5tjh6jvraq\" on table \"comments\"\n  Detail: Key (id)=(76e8a0b8-d718-47af-a3d1-ec8c27f18833) is still referenced from table \"comments\".] [delete from users where id=?]; SQL [delete from users where id=?]; constraint [fk8omq0tc18jd43bu5tjh6jvraq]",
	"details": {},
	"exceptionType": "DataIntegrityViolationException",
	"status": 500
	}*/
