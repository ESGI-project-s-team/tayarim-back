package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque l'email d'un administrateur est déjà utilisé dans le système.
 */
public class LogementAddressCreationError extends RuntimeException {

  public LogementAddressCreationError() {
    super("error_home_address_creation");
  }
}