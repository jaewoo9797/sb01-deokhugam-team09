package com.codeit.sb01_deokhugam.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	//global
	INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
	INVALID_REQUEST("잘못된 요청입니다"),
	ILLEGAL_ARGUMENT_ERROR("잘못된 인수가 전달되었습니다."),

	//book
	BOOK_NOT_FOUND("해당 도서가 존재하지 않습니다."),
	DUPLICATE_BOOK("동일한 도서가 존재합니다"),
	DUPLICATE_ISBN("동일한 ISBN이 존재합니다."),

	//thumbnail
	THUMBNAIL_NOT_FOUND("이미지가 없습니다."),

	//comment
	COMMENT_NOT_FOUND("해당 댓글이 존재하지 않습니다."),

	//notification
	NOTIFICATION_NOT_FOUND("해당 알림이 존재하지 않습니다."),

	//review
	REVIEW_NOT_FOUND("해당 리뷰가 존재하지 않습니다"),

	//user
	USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
	DUPLICATION_USER("이미 존재하는 사용자입니다."),
	LOGIN_INPUT_INVALID("이메일 또는 비밀번호가 올바르지 않습니다."),

	// auth
	UNAUTHORIZED("로그인을 해주세요."),
	ACCESS_DENIED("접근 권한이 없습니다."),

	//s3
	S3_UPLOAD_ERROR("S3 업로드 중 오류가 발생했습니다."),
	FILE_NAME_MISSING("파일 이름이 유효하지 않습니다.");

	private final String message;

}
