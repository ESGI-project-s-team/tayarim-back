package fr.esgi.al5_2.Tayarim.exceptions;

/**
 * Exception lancée lorsqu'une action est tentée sans les autorisations nécessaires.
 */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
    super("error_unauthorized");
  }
}