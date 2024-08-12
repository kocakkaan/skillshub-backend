package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoCompanyFound extends BaseException {

    public NoCompanyFound() {
        super(ErrorCode.NO_COMPANY_FOUND, "No company could be found.");
    }

    public NoCompanyFound(String message) {
        super(ErrorCode.NO_COMPANY_FOUND, message);
    }
    
}
