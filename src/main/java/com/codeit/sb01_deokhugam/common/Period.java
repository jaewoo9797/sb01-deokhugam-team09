package com.codeit.sb01_deokhugam.common;

// 파워유저 기간별 조회시에 사용하는 열거형
// 컨트롤러에서 받을때는 period라는 파라미터로 받게 된다.
//todo 인기도서 확인시에도 쓰이므로 공통클래스로 쓰는게 어떤지 물어보기
public enum Period {
	DAILY,
	WEEKLY,
	MONTHLY,
	ALL_TIME
}
