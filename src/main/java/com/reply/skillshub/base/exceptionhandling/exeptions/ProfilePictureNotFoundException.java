package com.reply.skillshub.base.exceptionhandling.exeptions;

public class ProfilePictureNotFoundException extends RuntimeException {
    public ProfilePictureNotFoundException() {
        super("Profile picture not found");
    }

    public ProfilePictureNotFoundException(String message) {
        super(message);
    }
}
