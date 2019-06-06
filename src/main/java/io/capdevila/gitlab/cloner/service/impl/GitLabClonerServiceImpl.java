package io.capdevila.gitlab.cloner.service.impl;

import io.capdevila.gitlab.cloner.exception.GitLabServiceApiException;
import io.capdevila.gitlab.cloner.exception.GitLabServiceProcessException;
import io.capdevila.gitlab.cloner.helper.GitCommandsHelper;
import io.capdevila.gitlab.cloner.helper.ProjectHelper;
import io.capdevila.gitlab.cloner.helper.SystemUtilsHelper;
import io.capdevila.gitlab.cloner.repository.impl.GitLab4JApiRepository;
import io.capdevila.gitlab.cloner.service.GitLabClonerService;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.Project;
import org.springframework.stereotype.Service;

//TODO: if final directoryPath does not exists it should be created?

/**
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GitLabClonerServiceImpl implements GitLabClonerService {


  private static final String PROJECT_GROUP = "project_group";
  private static final String USER_HOME = "user_home";
  private static final String LOG_DIRECTORY = "directory: {}";
  private static final String LOG_PROJECT_NAME = "projectName: {}";
  private static final String LOG_IS_SSH = "isSsh: {}";
  private static final String LOG_GROUP_NAME = "groupName: {}";
  private static final String LOG_DIRECTORY_PATH = "directoryPath: {}";
  private static final String LOG_OWNER_USERNAME = "ownerUsername: {}";

  private final GitLab4JApiRepository gitLab4JApiRepository;
  private final GitCommandsHelper gitCommandsHelper;
  private final SystemUtilsHelper systemUtilsHelper;
  private final ProjectHelper projectHelper;
  private final Executor executor;

  @Override
  public void cloneProject(@NonNull String directory, @NonNull String projectName, boolean isSsh) {
    log.debug(LOG_DIRECTORY, directory);
    log.debug(LOG_PROJECT_NAME, projectName);
    log.debug(LOG_IS_SSH, isSsh);
    cloneProjectInDirectory(gitLab4JApiRepository.getProject(projectName), directory, isSsh);
  }

  @Override
  public void cloneAllProjects(@NonNull String directory, boolean isSsh) {
    log.debug(LOG_DIRECTORY, directory);
    log.debug(LOG_IS_SSH, isSsh);
    gitLab4JApiRepository.getAllProjects()
        .forEach(project -> cloneProjectInDirectory(project, directory, isSsh));
  }

  @Override
  public void cloneAllProjectsByGroup(@NonNull String directory, boolean isSsh) {
    log.debug(LOG_DIRECTORY, directory);
    log.debug(LOG_IS_SSH, isSsh);
    gitLab4JApiRepository.getAllProjects()
        .forEach(project -> cloneProjectInDirectoryByGroup(directory, project, isSsh));
  }

  @Override
  public void cloneGroupProjects(@NonNull String directory, @NonNull String groupName,
      boolean isSsh) {
    log.debug(LOG_DIRECTORY, directory);
    log.debug(LOG_GROUP_NAME, groupName);
    log.debug(LOG_IS_SSH, isSsh);
    gitLab4JApiRepository.getAllProjectsByGroup(groupName)
        .forEach(project -> cloneProjectInDirectory(project, directory, isSsh));
  }

  @Override
  public void cloneUserProjects(@NonNull String directory, @NonNull String ownerUsername,
      boolean isSsh) {
    log.debug(LOG_DIRECTORY, directory);
    log.debug(LOG_OWNER_USERNAME, ownerUsername);
    log.debug(LOG_IS_SSH, isSsh);
    gitLab4JApiRepository.getAllProjectsByOwner(ownerUsername)
        .forEach(project -> cloneProjectInDirectory(project, directory, isSsh));
  }

  private void cloneProjectInDirectory(final Project project, final String directory,
      final boolean isSsh) {

    final ProcessBuilder processBuilder = new ProcessBuilder();

    final String directoryPath;
    if (directory.endsWith(PROJECT_GROUP)) {
      directoryPath = directory.replaceAll(PROJECT_GROUP, "")
          .concat(projectHelper.getProjectGroup(project));
    } else if (directory.equals(USER_HOME)) {
      directoryPath = systemUtilsHelper.getUserHome();
    } else {
      directoryPath = directory;
    }
    addDirectoryAndCommandAndExcecute(project, isSsh, processBuilder, directoryPath);
  }

  private void cloneProjectInDirectoryByGroup(final String directory, final Project project,
      final boolean isSsh) {

    final ProcessBuilder processBuilder = new ProcessBuilder();

    final String directoryPath;
    if (directory.equals(USER_HOME)) {
      directoryPath = systemUtilsHelper.getUserHome()
          .concat(projectHelper.getProjectGroup(project));
    } else {
      directoryPath = directory.concat(projectHelper.getProjectGroup(project));
    }
    addDirectoryAndCommandAndExcecute(project, isSsh, processBuilder, directoryPath);
  }

  private void addDirectoryAndCommandAndExcecute(Project project, boolean isSsh,
      ProcessBuilder processBuilder, String directoryPath) {
    log.debug(LOG_DIRECTORY_PATH, directoryPath);
    processBuilder.directory(new File(directoryPath));

    setUpCloneCommand(project, isSsh, processBuilder);

    int exitCode = buildAndExecuteProcess(processBuilder, log::info);
    if (exitCode != 0) {
      throw new GitLabServiceProcessException("Process failed with code: " + exitCode);
    }
  }

  private void setUpCloneCommand(Project project, boolean isSsh,
      ProcessBuilder processBuilder) {
    log.debug(LOG_PROJECT_NAME, project.getName());

    log.debug(LOG_IS_SSH, isSsh);
    final String cloneCommand;
    if (isSsh) {
      cloneCommand = gitCommandsHelper.getGitCloneCommand(project.getSshUrlToRepo());
    } else {
      cloneCommand = gitCommandsHelper.getGitCloneCommand(project.getHttpUrlToRepo());
    }

    log.debug("Add command: {}", cloneCommand);
    if (systemUtilsHelper.isOsWindows()) {
      log.debug("cmd.exe");
      processBuilder.command("cmd.exe", "/c", cloneCommand);
    } else {
      log.debug("sh");
      processBuilder.command("sh", "-c", cloneCommand);
    }

  }

  private int buildAndExecuteProcess(final ProcessBuilder processBuilder,
      Consumer<String> stringConsumer) {
    try {
      Process process = processBuilder.start();
      StreamGobbler streamGobbler =
          new StreamGobbler(process.getInputStream(), stringConsumer);
      executor.execute(streamGobbler);
      return process.waitFor();
    } catch (Exception exception) {
      throw new GitLabServiceApiException(exception);
    }
  }

  private static class StreamGobbler implements Runnable {

    private InputStream inputStream;
    private Consumer<String> consumer;

    private StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
      this.inputStream = inputStream;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(consumer);
    }
  }


}

