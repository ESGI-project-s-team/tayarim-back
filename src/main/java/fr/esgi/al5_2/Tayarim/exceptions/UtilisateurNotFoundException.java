package fr.esgi.al5_2.Tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class UtilisateurNotFoundException extends RuntimeException {

  public UtilisateurNotFoundException() {
    super("error_user_notExist");
  }
}