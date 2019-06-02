package io.capdevila.gitlab.cloner.config;

import io.capdevila.gitlab.cloner.config.properties.GitLabApiProperties;
import io.capdevila.gitlab.cloner.config.properties.GitLabClonerProperties;
import io.capdevila.gitlab.cloner.exception.GitLabApiConfigurationException;
import io.capdevila.gitlab.cloner.handler.GitLabClonerHandler;
import io.capdevila.gitlab.cloner.service.GitLabClonerService;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

/**
 * GitLab Cloner Configuration.
 *
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
@Slf4j
@Configuration
public class GitLabClonerConfiguration {

  /**
   * Instantiates a GitLabApi using GitLab4JApiBuilder from the given properties.
   *
   * @param gitLabApiProperties gitLabApiProperties.
   * @return GitLabAPi instance.
   */
  @Bean
  public GitLabApi gitLabApi(GitLabApiProperties gitLabApiProperties) {

    try {
      if (gitLabApiProperties.getPrivateToken() != null) {
        return new GitLab4JApiBuilder(gitLabApiProperties.getHostUrl())
            .build(gitLabApiProperties.getPrivateToken());
      }

      return new GitLab4JApiBuilder(gitLabApiProperties.getHostUrl())
          .build(gitLabApiProperties.getUsername(), gitLabApiProperties.getPassword());

    } catch (Exception exception) {
      throw new GitLabApiConfigurationException(exception);
    }

  }

  /**
   * Instantiates an Executor with the number of threads from the given properties.
   *
   * @param gitLabClonerProperties gitLabClonerProperties.
   * @return ConcurrentTaskExecutor instance.
   */
  @Bean
  public Executor getExecutor(GitLabClonerProperties gitLabClonerProperties) {
    return new ConcurrentTaskExecutor(
        Executors.newFixedThreadPool(gitLabClonerProperties.getThreads()));
  }

  @Profile("!test")
  @Bean
  public GitLabClonerHandler gitLabClonerHandler(GitLabClonerProperties gitLabClonerProperties, GitLabClonerService gitLabClonerService) {
    return new GitLabClonerHandler(gitLabClonerProperties, gitLabClonerService);
  }

  /**
   * GitLab4J custom builder to encapsulate the GitLabApi creation.
   *
   * @author Xavier Capdevila Estevez on 2019-05-29.
   */
  private class GitLab4JApiBuilder {

    @NonNull
    private String hostUrl;

    private GitLab4JApiBuilder(String hostUrl) {
      this.hostUrl = hostUrl;
    }

    /**
     * Builder from privateToken.
     *
     * @param privateToken privateToken.
     * @return GitLabAPi instance.
     */
    private GitLabApi build(@NonNull String privateToken) {
      return new GitLabApi(hostUrl, privateToken);
    }

    /**
     * Builder from username and password.
     *
     * @param username username.
     * @param password password.
     * @return GitLabAPi instance.
     * @throws GitLabApiException if credentials are invalid or there is a communication error.
     * @deprecated see {@link #build(String)}
     */
    @Deprecated
    private GitLabApi build(@NonNull String username, @NonNull String password) throws GitLabApiException {
      log.warn("Login authentication is @Deprecated!! Consider using a private token.");
      return GitLabApi.login(hostUrl, username, password);
    }

  }

}
