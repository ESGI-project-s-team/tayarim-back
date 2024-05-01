package fr.esgi.al5_2.Tayarim.exceptions;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
    super("error_unauthorized");
  }
}