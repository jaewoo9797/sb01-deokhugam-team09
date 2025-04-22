package com.codeit.sb01_deokhugam.domain.comment.exception;

import com.codeit.sb01_deokhugam.global.exception.DeokhugamException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;

public class CommentException extends DeokhugamException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
    
}
