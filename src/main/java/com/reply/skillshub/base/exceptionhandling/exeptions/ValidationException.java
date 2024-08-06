package com.reply.skillshub.base.exceptionhandling.exeptions;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BaseException {

    public ValidationException() {
        super(ErrorCode.VALIDATION_ERROR, "A validation error occurred");
    }

    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }

    public ValidationException(List<ObjectDetail> errors) {
        super(ErrorCode.VALIDATION_ERROR, "A validation error occurred", errors);
    }
    
}
