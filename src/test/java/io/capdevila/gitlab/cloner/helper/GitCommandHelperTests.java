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
public class GitCommandHelperTests {

  private static final String REPO_URL = "repo_url";

  private GitCommandHelper gitCommandHelper = new GitCommandHelper();

  @Test
  public void getGitCloneCommandIsWellFormatted() {
    String gitCloneCommand = gitCommandHelper.getGitCloneCommand(REPO_URL);
    Assertions.assertNotNull(gitCloneCommand);
    Assertions.assertEquals("git clone " + REPO_URL, gitCloneCommand);
  }

  @Test
  public void getGitCloneCommandNullArgThrowsNullPointerException() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> gitCommandHelper.getGitCloneCommand(null));
  }

}
