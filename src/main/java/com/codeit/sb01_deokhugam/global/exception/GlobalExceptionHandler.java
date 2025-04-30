package com.codeit.sb01_deokhugam.global.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		ErrorResponse errorResponse = new ErrorResponse(fieldErrors, HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
		log.error("필수 요청 파라미터가 누락되었습니다:  message = {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception, HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// 쿼리스트링/PathVariable → 타입 매핑 실패(enum, 숫자 등)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		log.error("Type Mapping Fail 잘못된 인수가 전달되었습니다:  message = {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception, HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// 지원하지 않는 Content-Type
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
		log.error("지원하지 않는 Content-Type 입니다:  message = {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
	}

	// 지원하지 않는 HTTP 메소드 요청 시
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		log.error("지원하지 않는 HTTP 메소드 요청입니다:  message = {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception, HttpStatus.METHOD_NOT_ALLOWED.value());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
	}

	/**
	 * 덕후감 예외에 대한 처리를 합니다.
	 *
	 * @param exception
	 * @return 해당 exception에 대한 에러 응답
	 */
	@ExceptionHandler(DeokhugamException.class)
	protected ResponseEntity<ErrorResponse> handleDeokhugamException(DeokhugamException exception) {
		log.error("커스텀 예외 발생: code={}, message = {}", exception.getErrorCode(), exception.getMessage());
		HttpStatus status = detemineHttpStatus(exception);
		ErrorResponse errorResponse = new ErrorResponse(exception, status.value());
		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception exception) {
		log.error("잘못된 인수가 전달되었습니다:  message = {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception, HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<ErrorResponse> handleRuntimeException(Exception exception) {
		ErrorResponse errorResponse = new ErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	/**
	 * 덕후감 예외에 대한 상태코드를 반환합니다.
	 *
	 * @param exception
	 * @return 해당 exception에 대한 상태코드
	 */
	private HttpStatus detemineHttpStatus(DeokhugamException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		return switch (errorCode) {
			case BOOK_NOT_FOUND, COMMENT_NOT_FOUND, NOTIFICATION_NOT_FOUND,
				 REVIEW_NOT_FOUND, USER_NOT_FOUND, THUMBNAIL_NOT_FOUND -> HttpStatus.NOT_FOUND;
			case INTERNAL_SERVER_ERROR, S3_UPLOAD_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
			case DUPLICATE_ISBN, DUPLICATE_BOOK -> HttpStatus.CONFLICT;
			case ILLEGAL_ARGUMENT_ERROR, INVALID_REQUEST, FILE_NAME_MISSING -> HttpStatus.BAD_REQUEST;
			case DUPLICATION_USER -> HttpStatus.CONFLICT;
			case LOGIN_INPUT_INVALID, UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;

		};
	}
}
