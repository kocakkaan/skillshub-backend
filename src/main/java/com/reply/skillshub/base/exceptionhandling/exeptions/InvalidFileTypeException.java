package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFileTypeException extends BaseException {

    public InvalidFileTypeException(String message) {
        super(ErrorCode.INVALID_FILE_TYPE, message);
    }

    public InvalidFileTypeException(String message, Throwable cause) {
        super(ErrorCode.INVALID_FILE_TYPE, message);
        if (cause != null) {
            initCause(cause);
        }
    }
}
