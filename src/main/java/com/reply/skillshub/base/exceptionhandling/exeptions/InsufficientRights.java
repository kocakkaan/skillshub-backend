package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientRights extends BaseException {
    
    public InsufficientRights() {
        super(ErrorCode.INSUFFICIENT_RIGHTS, "You do not have sufficient rights for this action");
    }

    public InsufficientRights(String message) {
        super(ErrorCode.INSUFFICIENT_RIGHTS, message);
    }
}
