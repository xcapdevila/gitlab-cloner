package io.capdevila.gitlab.cloner.repository;

import java.util.List;
import org.gitlab4j.api.models.Project;

/**
 * @author Xavier Capdevila Estevez on 2019-06-01.
 */
public interface GitLabApiRepository {

  Project getProject(String projectName);

  List<Project> getAllProjects();

  List<Project> getAllProjectsByGroup(String groupName);

  List<Project> getAllProjectsByOwner(String ownerUsername);

}
