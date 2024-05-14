package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque l'email d'un propriétaire est déjà utilisé dans le système.
 */
public class ProprietaireEmailAlreadyExistException extends RuntimeException {

  public ProprietaireEmailAlreadyExistException() {
    super("error_owner_alreadyExist_email");
  }
}