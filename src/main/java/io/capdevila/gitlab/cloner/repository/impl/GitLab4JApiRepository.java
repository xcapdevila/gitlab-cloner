package io.capdevila.gitlab.cloner.repository.impl;

import io.capdevila.gitlab.cloner.exception.GitLabApiCommunicationException;
import io.capdevila.gitlab.cloner.repository.GitLabApiRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Xavier Capdevila Estevez on 2019-06-01.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GitLab4JApiRepository implements GitLabApiRepository {

  private final GitLabApi gitLabApi;

  @Cacheable
  @Override
  public Project getProject(@NonNull String projectName) {
    try {
      log.debug("projectName: {}", projectName);
      return gitLabApi.getProjectApi().getProject(projectName);
    } catch (GitLabApiException gitLabApiException) {
      throw new GitLabApiCommunicationException(gitLabApiException);
    }
  }

  @Cacheable
  @Override
  public List<Project> getAllProjects() {
    try {
      return gitLabApi.getProjectApi().getProjects();
    } catch (GitLabApiException gitLabApiException) {
      throw new GitLabApiCommunicationException(gitLabApiException);
    }
  }

  @Cacheable
  @Override
  public List<Project> getAllProjectsByGroup(@NonNull String groupName) {
    try {
      log.debug("groupName: {}", groupName);
      return gitLabApi.getProjectApi().getProjects()
          .stream()
          .filter(project -> Objects.nonNull(project.getNameWithNamespace())
              && project.getNameWithNamespace().equalsIgnoreCase(groupName))
          .collect(Collectors.toList());
    } catch (GitLabApiException gitLabApiException) {
      throw new GitLabApiCommunicationException(gitLabApiException);
    }
  }

  @Cacheable
  @Override
  public List<Project> getAllProjectsByOwner(@NonNull String ownerUsername) {
    try {
      log.debug("ownerUsername: {}", ownerUsername);
      return gitLabApi.getProjectApi().getProjects()
          .stream()
          .filter(project -> Objects.nonNull(project)
              && project.getOwner().getUsername().equalsIgnoreCase(ownerUsername))
          .collect(Collectors.toList());
    } catch (GitLabApiException gitLabApiException) {
      throw new GitLabApiCommunicationException(gitLabApiException);
    }
  }
}
