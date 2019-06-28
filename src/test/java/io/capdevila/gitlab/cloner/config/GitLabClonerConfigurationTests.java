package io.capdevila.gitlab.cloner.config;

import io.capdevila.gitlab.cloner.config.properties.GitLabApiProperties;
import io.capdevila.gitlab.cloner.config.properties.GitLabClonerProperties;
import io.capdevila.gitlab.cloner.exception.GitLabApiConfigurationException;
import io.capdevila.gitlab.cloner.handler.GitLabClonerHandler;
import io.capdevila.gitlab.cloner.service.GitLabClonerService;
import org.gitlab4j.api.GitLabApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GitLabClonerConfigurationTests {

  private static final String PRIVATE_TOKEN = "PRIVATE_TOKEN";
  private static final String HOST_URL = "https://www.gitlab.com";
  private static final String USERNAME = "USERNAME";
  private static final String PASSWORD = "PASSWORD";

  @Mock
  private GitLabApiProperties gitLabApiProperties;

  @Mock
  private GitLabClonerProperties gitLabClonerProperties;

  @Mock
  private GitLabClonerService gitLabClonerService;

  private GitLabClonerConfiguration gitLabClonerConfiguration = new GitLabClonerConfiguration();

  @Test
  public void gitLabApiWithNullPropertiesThrowsGitLabApiConfigurationException() {

    Assertions.assertThrows(
        GitLabApiConfigurationException.class,
        () -> gitLabClonerConfiguration.gitLabApi(null));

  }

  @Test
  public void gitLabApiWithPrivateTokenAndMalformedHostUrl() {

    Mockito.when(gitLabApiProperties.getPrivateToken()).thenReturn(PRIVATE_TOKEN);
    Mockito.when(gitLabApiProperties.getHostUrl()).thenReturn(HOST_URL);

    GitLabApi gitLabApi = gitLabClonerConfiguration.gitLabApi(gitLabApiProperties);
    Assertions.assertNotNull(gitLabApi);

  }

  @Test
  public void gitLabApiWithUsernameAndPasswordAndMalformedHostUrlThrowsGitLabApiConfigurationException() {

    Mockito.when(gitLabApiProperties.getPrivateToken()).thenReturn(null);
    Mockito.when(gitLabApiProperties.getHostUrl()).thenReturn(HOST_URL);
    Mockito.when(gitLabApiProperties.getUsername()).thenReturn(USERNAME);
    Mockito.when(gitLabApiProperties.getPassword()).thenReturn(PASSWORD);

    Assertions.assertThrows(
        GitLabApiConfigurationException.class,
        () -> gitLabClonerConfiguration.gitLabApi(gitLabApiProperties));

  }

  @Test
  public void gitLabClonerHandlerWithMocks() {

    GitLabClonerHandler gitLabClonerHandler = gitLabClonerConfiguration.gitLabClonerHandler(gitLabClonerProperties, gitLabClonerService);
    Assertions.assertNotNull(gitLabClonerHandler);

  }

}
