package io.capdevila.gitlab.cloner;

import io.capdevila.gitlab.cloner.handler.GitLabClonerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class GitLabClonerApplication implements ApplicationRunner {

  @Autowired
  private ApplicationContext applicationContext;

  public static void main(String[] args) {
    SpringApplication.run(GitLabClonerApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {
    logApplicationArguments(args);

    try {
      GitLabClonerHandler gitLabClonerHandler = applicationContext
          .getBean(GitLabClonerHandler.class);
      gitLabClonerHandler.handle();
    } catch (BeansException beansException) {
      log.warn("Error getting GitLabClonerHandler bean.\nAre you using profile \"test\"?");
    }
  }

  private void logApplicationArguments(ApplicationArguments args) {

    final int nonOptionArgsSize = args.getNonOptionArgs().size();
    if (nonOptionArgsSize > 0) {
      log.debug("# NonOptionArgs: {}", nonOptionArgsSize);
      log.debug("NonOptionArgs: ");
      args.getNonOptionArgs().forEach(log::debug);
    }

    final int optionNamesSize = args.getOptionNames().size();
    if (optionNamesSize > 0) {
      log.debug("# OptionArgs: {}", optionNamesSize);
      log.debug("OptionArgs:");
      args.getOptionNames().forEach(optionName ->
          log.debug("{}={}", optionName, args.getOptionValues(optionName)));
    }

    if (nonOptionArgsSize == 0 && optionNamesSize == 0) {
      log.debug("No Args received.");
    }

  }

}
