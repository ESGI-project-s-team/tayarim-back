package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque l'email d'un administrateur est déjà utilisé dans le système.
 */
public class AdministrateurEmailAlreadyExistException extends RuntimeException {

  public AdministrateurEmailAlreadyExistException() {
    super("error_admin_alreadyExist_email");
  }
}