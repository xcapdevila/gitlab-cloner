package io.capdevila.gitlab.cloner.helper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.Project;
import org.springframework.stereotype.Component;

/**
 * @author Xavier Capdevila Estevez on 2019-06-06.
 */
@Slf4j
@Component
public class ProjectHelper {

  private static final String LOG_PROJECT_NAME = "projectName: {}";
  private static final String LOG_GROUP_NAME = "groupName: {}";

  /**
   * Extracts the project group.
   *
   * @param project project
   * @return project group
   */
  public String getProjectGroup(@NonNull Project project) {
    log.debug(LOG_PROJECT_NAME, project.getName());

    final String groupAndProjectName = project.getNameWithNamespace()
        .replaceAll(" ", "");
    final String groupName = groupAndProjectName
        .substring(0, groupAndProjectName.lastIndexOf('/'));
    log.debug(LOG_GROUP_NAME, groupName);

    return groupName;
  }

}
