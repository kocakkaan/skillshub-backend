package com.reply.skillshub.base.exceptionhandling.exeptions;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class BaseException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    @Getter
    private List<ObjectDetail> details = new ArrayList<>();


    protected BaseException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String message, List<ObjectDetail> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    
    
}
