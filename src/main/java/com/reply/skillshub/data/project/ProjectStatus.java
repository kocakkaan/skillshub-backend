package com.reply.skillshub.data.project;

public enum ProjectStatus {
    ACTIVE("ACTIVE"),
    COMPLETED("Completed"),
    ON_HOLD("On Hold"),
    CANCELLED("Cancelled");

    private final String status;

    ProjectStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
