package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un administrateur spécifié n'est pas trouvé dans le système.
 */
public class AdministrateurNotFoundException extends RuntimeException {

  public AdministrateurNotFoundException() {
    super("error_admin_notExist");
  }
}