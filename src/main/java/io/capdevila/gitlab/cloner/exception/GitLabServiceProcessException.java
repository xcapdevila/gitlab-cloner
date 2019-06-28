package io.capdevila.gitlab.cloner.exception;

/**
 * @author Xavier Capdevila Estevez on 2019-05-29.
 */
public class GitLabServiceProcessException extends RuntimeException {

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not initialized, and may subsequently be initialized by a call to {@link
   * #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
   */
  public GitLabServiceProcessException(String message) {
    super(message);
  }

}
