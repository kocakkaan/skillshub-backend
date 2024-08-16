package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidConfirmationToken extends BaseException {
    
    public InvalidConfirmationToken() {
        super(ErrorCode.INVALID_CONFIRMATION_TOKEN, "Invalid confirmation token");
    }

    public InvalidConfirmationToken(String message) {
        super(ErrorCode.INVALID_CONFIRMATION_TOKEN, message);
    }
}
