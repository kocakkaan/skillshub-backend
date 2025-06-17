package com.reply.skillshub.base.exceptionhandling.exeptions;

public class ProjectPictureNotDeletedException extends BaseException {
    public ProjectPictureNotDeletedException(String detailMessage) {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_DELETED,
                "The project picture could not be deleted. Details: " + detailMessage);
    }

    public ProjectPictureNotDeletedException() {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_DELETED,
                "An unexpected error occurred while deleting the project picture.");
    }

    public ProjectPictureNotDeletedException(String detailMessage, Throwable cause) {
        super(
                ErrorCode.PROJECT_PICTURE_NOT_DELETED,
                "The project picture could not be deleted. Details: " + detailMessage);
        if (cause != null) {
            initCause(cause);
        }
    }
}
