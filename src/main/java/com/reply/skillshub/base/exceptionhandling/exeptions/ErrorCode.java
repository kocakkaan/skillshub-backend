package com.reply.skillshub.base.exceptionhandling.exeptions;

public enum ErrorCode {

    DEFAULT("ERROR-001"),
    VALIDATION_ERROR("ERROR-002"),
    API_VALIDATION_ERROR("ERROR-003"),
    NO_COMPANY_FOUND("ERROR-004"),
    USER_NOT_FOUND("ERROR-005"),
    INSUFFICIENT_RIGHTS("ERROR-006"),
    INVALID_CONFIRMATION_TOKEN("ERROR-007"),
    UNCONFIRMED_USER("ERROR-008"),
    EXPERIENCE_NOT_FOUND("ERROR-009"),
    RESUME_NOT_FOUND("ERROR-010"),
    PROFILE_PICTURE_NOT_SAVED("ERROR-011"),
    PROJECT_PICTURE_NOT_SAVED("ERROR-012"),
    PROJECT_PICTURE_NOT_FOUND("ERROR-013"),
    INVALID_FILE_TYPE("ERROR-014"),
    PROFILE_PICTURE_NOT_FOUND("ERROR-015"),
    IMAGE_PROCESSING_ERROR("ERROR-016");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code + ": " + this.name();
    }

}
