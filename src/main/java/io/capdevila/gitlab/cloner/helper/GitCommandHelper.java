package io.capdevila.gitlab.cloner.helper;

import lombok.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author Xavier Capdevila Estevez on 2019-06-01.
 */
@Component
public class GitCommandHelper {

  @Cacheable(cacheNames = "getGitCloneCommand", key = "#urlToClone")
  public String getGitCloneCommand(@NonNull String urlToClone) {
    return String.format("git clone %s", urlToClone);
  }

}
