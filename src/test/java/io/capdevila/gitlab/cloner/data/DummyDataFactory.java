package io.capdevila.gitlab.cloner.data;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.gitlab4j.api.models.Owner;
import org.gitlab4j.api.models.Project;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * @author Xavier Capdevila Estevez on 2019-06-02.
 */
public class DummyDataFactory {

  private static final PodamFactory factory = new PodamFactoryImpl();

  private DummyDataFactory() {
    throw new UnsupportedOperationException("Factory class");
  }

  public static List<Project> getFakedProjects() {
    return getFakedProjects(Optional.empty(), Optional.empty(), Optional.empty());
  }

  public static List<Project> getFakedProjects(Integer numberOfProjects) {
    return getFakedProjects(Optional.empty(), Optional.empty(),
        Optional.ofNullable(numberOfProjects));
  }

  public static List<Project> getFakedProjects(String groupName, String username) {
    return getFakedProjects(Optional.ofNullable(groupName), Optional.ofNullable(username),
        Optional.empty());
  }

  public static List<Project> getFakedProjects(Optional<String> optionalGroupName,
      Optional<String> optionalUsername,
      Optional<Integer> optionalNumberOfProjects) {
    Faker faker = new Faker();

    final String groupName = optionalGroupName.orElse(faker.regexify("[a-z]{10}"));
    final String username = optionalUsername.orElse(faker.regexify("[a-z]{10}"));
    final Integer numberOfProjects = optionalNumberOfProjects.orElse(1);

    final List<Project> projects = new ArrayList<>();
    IntStream.range(0, numberOfProjects)
        .forEach(i -> projects.add(
            getFakedProject(Optional.ofNullable(groupName), null, Optional.ofNullable(username))));

    return projects;
  }

  public static List<Project> getFakedProjectsForGroup(String groupName,
      Integer numberOfProjects) {
    return getFakedProjects(Optional.ofNullable(groupName), Optional.empty(),
        Optional.ofNullable(numberOfProjects));
  }

  public static List<Project> getFakedProjectsForUsername(String username) {
    return getFakedProjects(Optional.empty(), Optional.ofNullable(username), Optional.empty());
  }

  public static List<Project> getFakedProjectsForUsername(String username,
      Integer numberOfProjects) {
    return getFakedProjects(Optional.empty(), Optional.ofNullable(username),
        Optional.ofNullable(numberOfProjects));
  }

  public static Project getFakedProjectsForProjectName(String projectName) {
    return getFakedProject(Optional.empty(), Optional.ofNullable(projectName), Optional.empty());
  }

  public static Project getFakedProject(Optional<String> optionalGroupName,
      Optional<String> optionalProjectName, Optional<String> optionalUsername) {
    Faker faker = new Faker();

    final String groupName = optionalGroupName.orElse(faker.regexify("[a-z]{10}"));
    final String projectName = optionalProjectName.orElse(faker.regexify("[a-z]{10}"));
    final String username = optionalUsername.orElse(faker.regexify("[a-z]{10}"));

    Project project = factory.manufacturePojo(Project.class);

    Owner owner = project.getOwner();
    owner.setUsername(username);
    owner.setName(owner.getUsername());
    owner.setEmail(owner.getUsername().concat("@gmail.com"));

    project.setName(projectName);
    project.setOwner(owner);
    project.setNameWithNamespace(groupName.concat(" / ").concat(project.getName()));
    project.setHttpUrlToRepo("https://");
    project.setHttpUrlToRepo("https://github.com/"
        .concat(project.getNameWithNamespace().replaceAll(" ", "").concat(".git")));
    project.setSshUrlToRepo("git@github.com:"
        .concat(project.getNameWithNamespace().replaceAll(" ", "").concat(".git")));

    return project;
  }

}
