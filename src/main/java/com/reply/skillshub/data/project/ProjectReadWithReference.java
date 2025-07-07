package com.reply.skillshub.data.project;

import java.util.List;

import com.reply.skillshub.data.project.reference.ProjectReference;

public interface ProjectReadWithReference extends ProjectRead {

    /**
     * Returns the project references associated with this project.
     *
     * @return a list of project references
     */
    List<ProjectReference> getReferences();

  
}
