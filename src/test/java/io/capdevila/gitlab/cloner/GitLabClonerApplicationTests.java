package io.capdevila.gitlab.cloner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GitLabClonerApplicationTests {

  @Test
  public void contextLoads() {
  }

  @Test
  public void runWithOptionArgs() {
    GitLabClonerApplication.main(new String[]{"--spring.profiles.active=test"});
  }

  @Test
  public void runWithAllArgs() {
    GitLabClonerApplication.main(new String[]{"--spring.profiles.active=test", "arg"});
  }

}
