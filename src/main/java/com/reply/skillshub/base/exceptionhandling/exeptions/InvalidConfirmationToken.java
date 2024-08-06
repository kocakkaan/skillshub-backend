package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidConfirmationToken extends BaseException {
    
    public InvalidConfirmationToken() {
        super(ErrorCode.VALIDATION_ERROR, "Invalid confirmation token");
    }

    public InvalidConfirmationToken(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}
