package io.capdevila.gitlab.cloner.config.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * GitLab Api Properties.
 *
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gitlab.api")
public class GitLabApiProperties {

  @NonNull
  private String hostUrl;

  private String privateToken;

  /**
   * @see #privateToken
   * @deprecated This parameter should be avoid. Use privateToken authentication instead.
   */
  @Deprecated
  private String username;

  /**
   * @see #privateToken
   * @deprecated This parameter should be avoid. Use privateToken authentication instead.
   */
  @Deprecated
  private String password;

}
