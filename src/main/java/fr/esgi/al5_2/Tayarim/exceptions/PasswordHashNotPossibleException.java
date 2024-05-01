package fr.esgi.al5_2.Tayarim.exceptions;

public class PasswordHashNotPossibleException extends RuntimeException {

  public PasswordHashNotPossibleException() {
    super("error_hash_password_impossible");
  }
}