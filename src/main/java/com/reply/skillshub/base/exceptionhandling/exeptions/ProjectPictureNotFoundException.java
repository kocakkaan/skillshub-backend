package com.reply.skillshub.base.exceptionhandling.exeptions;

public class ProjectPictureNotFoundException extends BaseException {
    public ProjectPictureNotFoundException(String detailMessage) {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_FOUND,
                "The project picture could not be found. Details: " + detailMessage);
    }

    public ProjectPictureNotFoundException() {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_FOUND,
                "The requested project picture does not exist or could not be accessed.");
    }

    public ProjectPictureNotFoundException(String detailMessage, Throwable cause) {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_FOUND,
                "The project picture could not be found. Details: " + detailMessage);
        if (cause != null) {
            initCause(cause);
        }
    }
}
