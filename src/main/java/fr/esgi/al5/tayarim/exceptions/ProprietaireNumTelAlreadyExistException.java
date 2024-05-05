package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque le numéro de téléphone d'un propriétaire est déjà utilisé dans le
 * système.
 */
public class ProprietaireNumTelAlreadyExistException extends RuntimeException {

  public ProprietaireNumTelAlreadyExistException() {
    super("error_owner_alreadyExist_numTel");
  }
}