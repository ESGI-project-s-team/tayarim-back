package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque le numéro de téléphone d'un administrateur est déjà utilisé dans le
 * système.
 */
public class AdministrateurNumTelAlreadyExistException extends RuntimeException {

  public AdministrateurNumTelAlreadyExistException() {
    super("error_admin_alreadyExist_numTel");
  }
}