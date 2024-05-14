package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'une action est tentée sans les autorisations nécessaires.
 */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
    super("error_unauthorized");
  }
}