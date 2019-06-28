package io.capdevila.gitlab.cloner.handler;

import io.capdevila.gitlab.cloner.config.properties.GitLabClonerProperties;
import io.capdevila.gitlab.cloner.config.properties.GitLabClonerProperties.GitLabClonerMode;
import io.capdevila.gitlab.cloner.exception.GitLabClonerConfigurationException;
import io.capdevila.gitlab.cloner.service.GitLabClonerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GitLabClonerHandlerTests {

  @MockBean
  private GitLabClonerService gitLabClonerService;

  @Test
  public void handleWithNullDirectoryThrowsGitLabClonerConfigurationException() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn(null);

    Assertions.assertThrows(GitLabClonerConfigurationException.class, () -> {
      new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();
    });

  }

  @Test
  public void handleWithNullModeThrowsNullPointerException() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(null);

    Assertions.assertThrows(NullPointerException.class, () -> {
      new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();
    });

  }

  @Test
  public void handleWithNonValidModeThrowsGitLabClonerConfigurationException() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.UNKNOWN);

    Assertions.assertThrows(GitLabClonerConfigurationException.class, () -> {
      new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();
    });

  }

  @Test
  public void handleAllProjects() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.ALL_PROJECTS);

    new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();

    Mockito.verify(gitLabClonerService, Mockito.times(1))
        .cloneAllProjects(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
  }

  @Test
  public void handleAllProjectsByGroup() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode())
        .thenReturn(GitLabClonerMode.ALL_PROJECTS_BY_GROUP);

    new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();

    Mockito.verify(gitLabClonerService, Mockito.times(1))
        .cloneAllProjectsByGroup(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
  }

  @Test
  public void handleGroupProjectsWithNullGroupNameThrowsGitLabClonerConfigurationException() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.GROUP_PROJECTS);

    Assertions.assertThrows(GitLabClonerConfigurationException.class, () -> {
      new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();
    });

  }

  @Test
  public void handleGroupProjectsWithGroupName() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.GROUP_PROJECTS);
    Mockito.when(gitLabClonerProperties.getGroupName()).thenReturn("test");

    new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();

    Mockito.verify(gitLabClonerService, Mockito.times(1))
        .cloneGroupProjects(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
            ArgumentMatchers.anyBoolean());
  }

  @Test
  public void handleUserProjectsWithNullOwnerUsernameThrowsGitLabClonerConfigurationException() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.USER_PROJECTS);

    Assertions.assertThrows(GitLabClonerConfigurationException.class, () -> {
      new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();
    });

  }

  @Test
  public void handleUserProjectsWithOwnerUsername() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.USER_PROJECTS);
    Mockito.when(gitLabClonerProperties.getOwnerUsername()).thenReturn("test");

    new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();

    Mockito.verify(gitLabClonerService, Mockito.times(1))
        .cloneUserProjects(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
            ArgumentMatchers.anyBoolean());
  }

  @Test
  public void handleSingleProjectsWithNullProjectNameThrowsGitLabClonerConfigurationException() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.SINGLE_PROJECT);

    Assertions.assertThrows(GitLabClonerConfigurationException.class, () -> {
      new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();
    });

  }

  @Test
  public void handleSingleProjectsWithProjectName() {

    GitLabClonerProperties gitLabClonerProperties = Mockito.mock(GitLabClonerProperties.class);
    Mockito.when(gitLabClonerProperties.getDirectory()).thenReturn("user_home");
    Mockito.when(gitLabClonerProperties.getMode()).thenReturn(GitLabClonerMode.SINGLE_PROJECT);
    Mockito.when(gitLabClonerProperties.getProjectName()).thenReturn("test");

    new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService).handle();

    Mockito.verify(gitLabClonerService, Mockito.times(1))
        .cloneProject(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
            ArgumentMatchers.anyBoolean());
  }

}
