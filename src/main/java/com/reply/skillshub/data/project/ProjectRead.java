package com.reply.skillshub.data.project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRead {

    String getId();

    int getProjectId();

    String getTitle();

    String getDescription();

    String getCompany();

    List<Client> getClients();

    Contact getContact();

    List<ProjectType> getProjectType();

    boolean getIsPublic();

    boolean getIsPublicSector();

    LocalDate getStartDate();

    LocalDate getEndDate();

    Double getRevenue();

    User getManager();

    List<User> getUsers();

    // String getStatus();

    List<Skill> getTechnologies();

    Industry getIndustry();

    String getProjectPictureLocation();

    interface User {
        String getId();

        String getFirstName();

        String getLastName();

        default String getFullName() {
            return getFirstName().concat(" ").concat(getLastName());
        }
    }

    interface Client {
        String getId();

        String getName();
    }

    interface Contact {
        String getId();

        String getName();

        String getEmail();

        String getPhoneNumber();

        Company getCompany();

        interface Company {
            String getId();

            String getName();
        }
    }

    interface ProjectType {
        String getName();
    }

    interface Industry {
        String getId();

        String getLabel();
    }

    interface Skill {
        String getId();

        String getLabel();
    }

    public default String getFormattedProjectId() {
        return "ID" + String.format("%04d", this.getProjectId());
    }

}
