package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfilePictureNotFoundException extends BaseException {
    public ProfilePictureNotFoundException() {
        super(ErrorCode.PROFILE_PICTURE_NOT_FOUND, "Profile picture not found");
    }

    public ProfilePictureNotFoundException(String message) {
        super(ErrorCode.PROFILE_PICTURE_NOT_FOUND, message);
    }
}
