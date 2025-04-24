package com.codeit.sb01_deokhugam.global.s3.exception;

import com.codeit.sb01_deokhugam.global.exception.DeokhugamException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class S3UploadException extends DeokhugamException {

	public S3UploadException(ErrorCode errorCode) {
		super(errorCode);
	}

	public S3UploadException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
