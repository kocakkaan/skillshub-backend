package com.reply.skillshub.base.exceptionhandling.exeptions;

public class ProjectPictureNotSavedException extends BaseException {
    public ProjectPictureNotSavedException(String detailMessage) {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_SAVED,
                "The project picture could not be saved. Details: " + detailMessage);
    }

    public ProjectPictureNotSavedException() {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_SAVED,
                "An unexpected error occurred while saving the project picture.");
    }
}
