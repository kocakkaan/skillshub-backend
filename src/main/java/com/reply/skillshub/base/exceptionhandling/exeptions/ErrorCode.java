package com.reply.skillshub.base.exceptionhandling.exeptions;

public enum ErrorCode {

    DEFAULT("ERROR-001"),
    VALIDATION_ERROR("ERROR-002"),
    API_VALIDATION_ERROR("ERROR-003"),
    NO_COMPANY_FOUND("ERROR-004"),
    USER_NOT_FOUND("ERROR-005"),
    INSUFFICIENT_RIGHTS("ERROR-006"),
    INVALID_CONFIRMATION_TOKEN("ERROR-007"),
    UNCONFIRMED_USER("ERROR-008");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code + ": " + this.name();
    }
    
}
