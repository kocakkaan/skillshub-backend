package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class UnconfirmedUser extends BaseException {
    
    public UnconfirmedUser() {
        super(ErrorCode.UNCONFIRMED_USER, "The user has not been confirmed");
    }

    public UnconfirmedUser(String message) {
        super(ErrorCode.UNCONFIRMED_USER, message);
    }
}
