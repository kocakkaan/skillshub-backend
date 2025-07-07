package com.reply.skillshub.data.project;

import java.util.List;

public interface ProjectRead {

    String getId();

    int getProjectId();

    String getTitle();

    String getDescription();

    List<Client> getClients();

    List<ProjectType> getProjectType();

    // String getStatus();

    List<Skill> getTechnologies();

    Industry getIndustry();

    String getProjectPictureLocation();

    interface Client {
        String getId();

        String getName();
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
