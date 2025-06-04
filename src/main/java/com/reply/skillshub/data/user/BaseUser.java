package com.reply.skillshub.data.user;

import java.time.LocalDate;

import com.reply.skillshub.data.userrole.UserRole;

public interface BaseUser {
    public String getId();

    public String getFirstName();

    public String getLastName();

    public String getProfilePictureLocation();

    public String getEmail();

    public String getPhoneNumber();

    public UserRole getUserRole();

    public String getPassword();

    public String getConfirmationToken();

    public Boolean getConfirmed();

    public CreatedBy getCreatedBy();

    public LocalDate getCreatedOn();

    default String getFullName() {
        return getFirstName().concat(" ").concat(getLastName());
    }

    interface CreatedBy {
        public String getId();

        public String getFirstName();

        public String getLastName();

        default String getFullName() {
            return getFirstName().concat(" ").concat(getLastName());
        }

    }
}
