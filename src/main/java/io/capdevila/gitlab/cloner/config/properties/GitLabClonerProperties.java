package io.capdevila.gitlab.cloner.config.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * GitLab Cloner Properties.
 *
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gitlab.cloner")
public class GitLabClonerProperties {

  @NonNull
  private GitLabClonerMode mode = GitLabClonerMode.ALL_PROJECTS;

  private int threads = 25;

  @NonNull
  private String directory;

  private boolean ssh;

  private String groupName;

  private String ownerUsername;

  private String projectName;

  /**
   * GitLab Cloner Mode.
   *
   * @author Xavier Capdevila Estevez on 2019-05-29.
   */
  public enum GitLabClonerMode {
    SINGLE_PROJECT,
    ALL_PROJECTS,
    ALL_PROJECTS_BY_GROUP,
    GROUP_PROJECTS,
    USER_PROJECTS,
    UNKNOWN
  }

}
