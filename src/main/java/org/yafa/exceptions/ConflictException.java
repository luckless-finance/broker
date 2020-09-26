package org.yafa.exceptions;

public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }

  public ConflictException(Throwable throwable) {
    super(throwable);
  }

  public ConflictException(Exception exception) {
    super(exception);
  }
}
