package io.capdevila.gitlab.cloner.service.impl;

import io.capdevila.gitlab.cloner.data.DummyDataFactory;
import io.capdevila.gitlab.cloner.exception.GitLabServiceApiException;
import io.capdevila.gitlab.cloner.exception.GitLabServiceProcessException;
import io.capdevila.gitlab.cloner.helper.GitCommandHelper;
import io.capdevila.gitlab.cloner.helper.ProjectHelper;
import io.capdevila.gitlab.cloner.helper.SystemUtilsHelper;
import io.capdevila.gitlab.cloner.repository.impl.GitLab4JApiRepository;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.SystemUtils;
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
public class GitLabClonerServiceImplTests {

  private static final String PROJECT_GROUP_DIRECTORY = "project_group";
  private static final String USER_HOME_DIRECTORY = "user_home";
  private static final String TEST_DIRECTORY = "./";
  private static final String TEST_GROUP = "test-group";
  private static final String TEST_USER = "test";
  private static final String TEST_PROJECT = "test-project";

  @MockBean
  private GitLab4JApiRepository gitLab4JApiRepository;

  @MockBean
  private GitCommandHelper gitCommandHelper;

  @MockBean
  private SystemUtilsHelper systemUtilsHelper;

  @MockBean
  private ProjectHelper projectHelper;

  @MockBean
  private Executor executor;


  @BeforeEach
  public void setUp() {
    Mockito.when(gitCommandHelper.getGitCloneCommand(ArgumentMatchers.anyString()))
        .thenReturn("ls");

    Mockito.when(systemUtilsHelper.isOsWindows())
        .thenReturn(SystemUtils.IS_OS_WINDOWS);
    Mockito.when(systemUtilsHelper.getUserHome())
        .thenReturn(System.getProperty("user.home").concat("/"));

    Mockito.when(projectHelper.getProjectGroup(ArgumentMatchers.any(Project.class)))
        .thenReturn(TEST_GROUP);

    Mockito.when(gitLab4JApiRepository.getAllProjects())
        .thenReturn(DummyDataFactory.getFakedProjects());
    Mockito.when(gitLab4JApiRepository.getAllProjectsByGroup(ArgumentMatchers.anyString()))
        .thenReturn(DummyDataFactory.getFakedProjectsForGroup(TEST_GROUP, 1));
    Mockito.when(gitLab4JApiRepository.getAllProjectsByOwner(ArgumentMatchers.anyString()))
        .thenReturn(DummyDataFactory.getFakedProjectsForUsername(TEST_USER));
    Mockito.when(gitLab4JApiRepository.getProject(ArgumentMatchers.anyString()))
        .thenReturn(DummyDataFactory.getFakedProject(TEST_GROUP, TEST_PROJECT, TEST_USER));
  }

  @Test
  public void cloneProjectWithNullDirectoryThrowsNullPointerException() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    final String projectName = TEST_PROJECT;
    final boolean isSsh = false;

    Assertions.assertThrows(NullPointerException.class, () -> {
      gitLabClonerServiceImpl.cloneProject(null, projectName, isSsh);
    });

  }

  @Test
  public void cloneProjectWithNullProjectNameThrowsNullPointerException() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    final boolean isSsh = false;

    Assertions.assertThrows(NullPointerException.class, () -> {
      gitLabClonerServiceImpl.cloneProject(TEST_DIRECTORY, null, isSsh);
    });

  }

  @Test
  public void cloneProjectHttps() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    gitLabClonerServiceImpl.cloneProject(TEST_DIRECTORY, TEST_PROJECT, false);

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

    Mockito.verify(executor, Mockito.times(1))
        .execute(ArgumentMatchers.any(Runnable.class));

  }

  @Test
  public void cloneProjectHttpsWithExecutorException() {

    Mockito.doThrow(RuntimeException.class).when(executor)
        .execute(ArgumentMatchers.any(Runnable.class));

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    Assertions.assertThrows(GitLabServiceApiException.class, () -> {
      gitLabClonerServiceImpl.cloneProject(TEST_DIRECTORY, TEST_PROJECT, false);
    });

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

    Mockito.verify(executor, Mockito.times(1))
        .execute(ArgumentMatchers.any(Runnable.class));

  }

  @Test
  public void cloneProjectSsh() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    gitLabClonerServiceImpl.cloneProject(TEST_DIRECTORY, TEST_PROJECT, true);

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

    Mockito.verify(executor, Mockito.times(1))
        .execute(ArgumentMatchers.any(Runnable.class));

  }

  @Test
  public void cloneProjectHttpsInOtherOSThrowsGitLabServiceApiException() {

    Mockito.when(systemUtilsHelper.isOsWindows()).thenReturn(!SystemUtils.IS_OS_WINDOWS);

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    Assertions.assertThrows(GitLabServiceApiException.class, () -> {
      gitLabClonerServiceImpl.cloneProject(TEST_DIRECTORY, TEST_PROJECT, false);
    });

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

  }

  @Test
  public void cloneProjectHttpsWithUserHomeDirectory() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    gitLabClonerServiceImpl.cloneProject(USER_HOME_DIRECTORY, TEST_PROJECT, false);

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .getUserHome();

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

    Mockito.verify(executor, Mockito.times(1))
        .execute(ArgumentMatchers.any(Runnable.class));

  }

  @Test
  public void cloneProjectHttpsWithProjectGroupDirectoryThrowsGitLabServiceApiException() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    Assertions.assertThrows(GitLabServiceApiException.class, () -> {
      gitLabClonerServiceImpl.cloneProject(PROJECT_GROUP_DIRECTORY, TEST_PROJECT, false);
    });

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

  }

  @Test
  public void cloneProjectSshWithFakeCommandAndRealExecutorThrowsGitLabServiceProcessException() {

    Mockito.when(gitCommandHelper.getGitCloneCommand(ArgumentMatchers.anyString()))
        .thenReturn("fake_command");

    Executor realExecutor = Executors.newSingleThreadExecutor();

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper,
        realExecutor);

    Assertions.assertThrows(GitLabServiceProcessException.class, () ->
        gitLabClonerServiceImpl.cloneProject(TEST_DIRECTORY, TEST_PROJECT, true)
    );

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getProject(ArgumentMatchers.anyString());

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

  }

  @Test
  public void cloneAllProjectsHttps() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    gitLabClonerServiceImpl.cloneAllProjects(USER_HOME_DIRECTORY, false);

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getAllProjects();

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

    Mockito.verify(executor, Mockito.times(1))
        .execute(ArgumentMatchers.any(Runnable.class));

  }

  @Test
  public void cloneGroupProjectsHttps() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    gitLabClonerServiceImpl.cloneGroupProjects(USER_HOME_DIRECTORY, TEST_GROUP, false);

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getAllProjectsByGroup(TEST_GROUP);

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

    Mockito.verify(executor, Mockito.times(1))
        .execute(ArgumentMatchers.any(Runnable.class));

  }

  @Test
  public void cloneUserProjectsHttps() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    gitLabClonerServiceImpl.cloneUserProjects(USER_HOME_DIRECTORY, TEST_USER, false);

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getAllProjectsByOwner(TEST_USER);

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

    Mockito.verify(executor, Mockito.times(1))
        .execute(ArgumentMatchers.any(Runnable.class));

  }

  @Test
  public void cloneAllProjectsByGroupHttpsThrowsGitLabServiceApiException() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    Assertions.assertThrows(GitLabServiceApiException.class, () ->
        gitLabClonerServiceImpl.cloneAllProjectsByGroup(USER_HOME_DIRECTORY, false));

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getAllProjects();

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

  }

  @Test
  public void cloneAllProjectsByGroupHttpsWithNotHomeDirectoryThrowsGitLabServiceApiException() {

    final GitLabClonerServiceImpl gitLabClonerServiceImpl = new GitLabClonerServiceImpl(
        gitLab4JApiRepository, gitCommandHelper, systemUtilsHelper, projectHelper, executor);

    Assertions.assertThrows(GitLabServiceApiException.class, () ->
        gitLabClonerServiceImpl.cloneAllProjectsByGroup(TEST_DIRECTORY, false));

    Mockito.verify(gitLab4JApiRepository, Mockito.times(1))
        .getAllProjects();

    Mockito.verify(gitCommandHelper, Mockito.times(1))
        .getGitCloneCommand(ArgumentMatchers.anyString());

    Mockito.verify(systemUtilsHelper, Mockito.times(1))
        .isOsWindows();

  }

}
