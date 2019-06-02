package io.capdevila.gitlab.cloner.data;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
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

  public static List<Project> getFakedProjects() {
    return getFakedProjects(null, null, null);
  }

  public static List<Project> getFakedProjects(String gName, String uName) {
    return getFakedProjects(gName, uName, null);
  }

  public static List<Project> getFakedProjects(String gName, String uName, Integer numberOfProjects) {
    Faker faker = new Faker();

    final String groupName;
    if (gName == null) {
      groupName = faker.regexify("[a-z]{10}");
    } else {
      groupName = gName;
    }

    final String username;
    if (uName == null) {
      username = faker.regexify("[a-z]{10}");
    } else {
      username = uName;
    }

    if (numberOfProjects == null) {
      numberOfProjects = 1;
    }

    final List<Project> projects = new ArrayList<>();
    IntStream.range(0, numberOfProjects).forEach(i -> projects.add(getFakedProject(groupName, null, username)));

    return projects;
  }

  public static List<Project> getFakedProjectsForGroup(String gName) {
    return getFakedProjects(gName, null, null);
  }

  public static List<Project> getFakedProjectsForUsername(String uName) {
    return getFakedProjects(null, uName, null);
  }

  public static Project getFakedProject(String groupName, String projectName, String username) {
    Faker faker = new Faker();

    if (groupName == null) {
      groupName = faker.regexify("[a-z]{10}");
    }
    if (projectName == null) {
      projectName = faker.regexify("[a-z]{10}");
    }
    if (username == null) {
      username = faker.regexify("[a-z]{10}");
    }

    Project project = factory.manufacturePojo(Project.class);

    Owner owner = project.getOwner();
    owner.setUsername(username);
    owner.setName(owner.getUsername());
    owner.setEmail(owner.getUsername().concat("@gmail.com"));

    project.setName(projectName);
    project.setOwner(owner);
    project.setNameWithNamespace(groupName.concat(" / ").concat(project.getName()));
    project.setHttpUrlToRepo("https://");
    project.setHttpUrlToRepo("https://github.com/".concat(project.getNameWithNamespace().replaceAll(" ", "").concat(".git")));
    project.setSshUrlToRepo("git@github.com:".concat(project.getNameWithNamespace().replaceAll(" ", "").concat(".git")));

    return project;
  }

}
