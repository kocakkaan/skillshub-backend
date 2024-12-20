package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResumeSkillNotFound extends BaseException {

    public ResumeSkillNotFound() {
        super(ErrorCode.USER_NOT_FOUND, "No resume skill could be found.");
    }

    public ResumeSkillNotFound(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}
