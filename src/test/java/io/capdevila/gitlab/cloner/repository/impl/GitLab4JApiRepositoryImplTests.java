package io.capdevila.gitlab.cloner.repository.impl;

import io.capdevila.gitlab.cloner.data.DummyDataFactory;
import io.capdevila.gitlab.cloner.exception.GitLabApiCommunicationException;
import io.capdevila.gitlab.cloner.helper.ProjectHelper;
import java.util.List;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.models.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Xavier Capdevila Estevez on 2019-06-02.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GitLab4JApiRepositoryImplTests {

  private static final String TEST_GROUP = "test-group";
  private static final String TEST_USER = "test";
  private static final String TEST_PROJECT = "test-project";

  @MockBean
  private GitLabApi gitLabApi;

  @MockBean
  private ProjectApi projectApi;

  private ProjectHelper projectHelper = new ProjectHelper();

  @BeforeEach
  public void setUp() throws GitLabApiException {

    Mockito.when(gitLabApi.getProjectApi()).thenReturn(projectApi);

  }

  @Test
  public void getProject() throws GitLabApiException {

    final Project fakedProject = DummyDataFactory
        .getFakedProjectsForProjectName(TEST_PROJECT);

    Mockito.when(projectApi.getProject(ArgumentMatchers.anyString()))
        .thenReturn(fakedProject);

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    final Project project = gitLab4JApiRepository.getProject(TEST_PROJECT);

    Assertions.assertEquals(fakedProject, project);

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

  }

  @Test
  public void getProjectThrowsGitLabApiCommunicationException() throws GitLabApiException {

    Mockito.doThrow(GitLabApiException.class).when(projectApi)
        .getProject(ArgumentMatchers.anyString());

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    Assertions.assertThrows(GitLabApiCommunicationException.class, () -> {
      gitLab4JApiRepository.getProject(TEST_PROJECT);
    });

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

  }

  @Test
  public void getAllProjects() throws GitLabApiException {

    final List<Project> fakedProjects = DummyDataFactory
        .getFakedProjects(10);

    Mockito.when(projectApi.getProjects())
        .thenReturn(fakedProjects);

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    final List<Project> projects = gitLab4JApiRepository.getAllProjects();

    Assertions.assertEquals(fakedProjects, projects);

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProjects();

  }

  @Test
  public void getAllProjectsThrowsGitLabApiCommunicationException() throws GitLabApiException {

    Mockito.doThrow(GitLabApiException.class).when(projectApi)
        .getProjects();

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    Assertions.assertThrows(GitLabApiCommunicationException.class, () -> {
      gitLab4JApiRepository.getAllProjects();
    });

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProjects();

  }

  @Test
  public void getAllProjectsByGroup() throws GitLabApiException {

    final List<Project> fakedProjects = DummyDataFactory
        .getFakedProjectsForGroup(TEST_GROUP, 10);

    Mockito.when(projectApi.getProjects())
        .thenReturn(fakedProjects);

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    final List<Project> projects = gitLab4JApiRepository.getAllProjectsByGroup(TEST_GROUP);

    Assertions.assertEquals(fakedProjects, projects);

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProjects();

  }

  @Test
  public void getAllProjectsByGroupThrowsGitLabApiCommunicationException()
      throws GitLabApiException {

    Mockito.doThrow(GitLabApiException.class).when(projectApi)
        .getProjects();

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    Assertions.assertThrows(GitLabApiCommunicationException.class, () -> {
      gitLab4JApiRepository.getAllProjectsByGroup(TEST_GROUP);
    });

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProjects();

  }

  @Test
  public void getAllProjectsByOwner() throws GitLabApiException {

    final List<Project> fakedProjects = DummyDataFactory
        .getFakedProjectsForUsername(TEST_USER, 10);

    Mockito.when(projectApi.getProjects())
        .thenReturn(fakedProjects);

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    final List<Project> projects = gitLab4JApiRepository.getAllProjectsByOwner(TEST_USER);

    Assertions.assertEquals(fakedProjects, projects);

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProjects();

  }

  @Test
  public void getAllProjectsByOwnerThrowsGitLabApiCommunicationException()
      throws GitLabApiException {

    Mockito.doThrow(GitLabApiException.class).when(projectApi)
        .getProjects();

    final GitLab4JApiRepository gitLab4JApiRepository = new GitLab4JApiRepository(gitLabApi,
        projectHelper);

    Assertions.assertThrows(GitLabApiCommunicationException.class, () -> {
      gitLab4JApiRepository.getAllProjectsByOwner(TEST_USER);
    });

    Mockito.verify(gitLabApi, Mockito.times(1))
        .getProjectApi();

    Mockito.verify(projectApi, Mockito.times(1))
        .getProjects();

  }

}
