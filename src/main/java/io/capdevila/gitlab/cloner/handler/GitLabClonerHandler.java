package io.capdevila.gitlab.cloner.handler;

import io.capdevila.gitlab.cloner.config.properties.GitLabClonerProperties;
import io.capdevila.gitlab.cloner.config.properties.GitLabClonerProperties.GitLabClonerMode;
import io.capdevila.gitlab.cloner.exception.GitLabClonerConfigurationException;
import io.capdevila.gitlab.cloner.service.GitLabClonerService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
@Slf4j
@RequiredArgsConstructor
public class GitLabClonerHandler {

  private final GitLabClonerProperties gitLabClonerProperties;
  private final GitLabClonerService gitLabClonerService;

  /**
   * Entrypoint to handle the GitLab Cloner execution flow.
   */
  public void handle() {

    final String directory = getRequiredValue(gitLabClonerProperties.getDirectory());
    log.debug("directory: {}", directory);
    final boolean isSsh = gitLabClonerProperties.isSsh();
    log.debug("isSsh: {}", isSsh);
    final GitLabClonerMode mode = gitLabClonerProperties.getMode();
    log.debug("mode: {}", mode);

    switch (mode) {
      case ALL_PROJECTS:
        gitLabClonerService.cloneAllProjects(directory, isSsh);
        break;
      case ALL_PROJECTS_BY_GROUP:
        gitLabClonerService.cloneAllProjectsByGroup(directory, isSsh);
        break;
      case GROUP_PROJECTS:
        final String groupName = getRequiredValue(gitLabClonerProperties.getGroupName());
        gitLabClonerService.cloneGroupProjects(directory, groupName, isSsh);
        break;
      case USER_PROJECTS:
        final String ownerUsername = Optional.ofNullable(gitLabClonerProperties.getOwnerUsername())
            .orElseThrow(GitLabClonerConfigurationException::new);
        gitLabClonerService.cloneUserProjects(directory, ownerUsername, isSsh);
        break;
      case SINGLE_PROJECT:
        final String projectName = Optional.ofNullable(gitLabClonerProperties.getProjectName())
            .orElseThrow(GitLabClonerConfigurationException::new);
        gitLabClonerService.cloneProject(directory, projectName, isSsh);
        break;
      default:
        throw new GitLabClonerConfigurationException();
    }

  }

  private String getRequiredValue(String value) {
    return Optional.ofNullable(value)
        .orElseThrow(GitLabClonerConfigurationException::new);
  }

}

