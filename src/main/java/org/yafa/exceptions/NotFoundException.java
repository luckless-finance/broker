package org.yafa.exceptions;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(Throwable throwable) {
    super(throwable);
  }

  public NotFoundException(Exception exception) {
    super(exception);
  }
}
