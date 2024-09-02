package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResumeNotFound extends BaseException {

    public ResumeNotFound() {
        super(ErrorCode.USER_NOT_FOUND, "No Resume could be found.");
    }

    public ResumeNotFound(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}
