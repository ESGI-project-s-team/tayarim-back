package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un propriétaire spécifié n'est pas trouvé dans le système.
 */
public class ProprietaireNotFoundException extends RuntimeException {

  public ProprietaireNotFoundException() {
    super("error_owner_notExist");
  }
}