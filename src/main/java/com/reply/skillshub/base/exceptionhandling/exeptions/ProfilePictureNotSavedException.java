package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ProfilePictureNotSavedException extends BaseException {
    public ProfilePictureNotSavedException() {
        super(ErrorCode.PROFILE_PICTURE_NOT_SAVED, "Something went wrong when processing the profile picture");
    }

    public ProfilePictureNotSavedException(String message) {
        super(ErrorCode.PROFILE_PICTURE_NOT_SAVED, message);
    }
}