package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends BaseException {
    public UserNotFound() {
        super(ErrorCode.USER_NOT_FOUND, "No user could be found.");
    }

    public UserNotFound(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}
