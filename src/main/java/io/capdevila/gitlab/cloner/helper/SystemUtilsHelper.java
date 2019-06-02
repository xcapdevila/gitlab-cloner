package io.capdevila.gitlab.cloner.helper;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
@Component
public class SystemUtilsHelper {

  @Cacheable("isOsWindows")
  public boolean isOsWindows() {
    return SystemUtils.IS_OS_WINDOWS;
  }

  @Cacheable("getUserHome")
  public String getUserHome() {
    return System.getProperty("user.home").concat("/");
  }

}
