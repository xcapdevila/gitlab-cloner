package io.capdevila.gitlab.cloner.service;

/**
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
public interface GitLabClonerService {

  void cloneProject(String directory, String projectName, boolean isSsh);

  void cloneAllProjects(String directory, boolean isSsh);

  void cloneAllProjectsByGroup(String directory, boolean isSsh);

  void cloneGroupProjects(String directory, String groupName, boolean isSsh);

  void cloneUserProjects(String directory, String ownerUsername, boolean isSsh);

}
