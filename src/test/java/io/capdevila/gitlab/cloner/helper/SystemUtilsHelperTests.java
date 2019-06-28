package io.capdevila.gitlab.cloner.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SystemUtilsHelperTests {

  private SystemUtilsHelper systemUtilsHelper = new SystemUtilsHelper();

  @Test
  public void getUserHomeIsNotNull() {
    String userHome = systemUtilsHelper.getUserHome();
    Assertions.assertNotNull(userHome);
    Assertions.assertTrue(userHome.endsWith("/"));
  }

  @Test
  public void isWindowsDoesNotThrowExceptions() {
    Assertions.assertDoesNotThrow(() -> systemUtilsHelper.isOsWindows());
  }

}
