package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque le hashage du mot de passe est impossible à réaliser.
 */
public class PasswordHashNotPossibleException extends RuntimeException {

  public PasswordHashNotPossibleException() {
    super("error_hash_password_impossible");
  }
}