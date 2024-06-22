package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class IndisponibiliteNotFoundError extends RuntimeException {

  public IndisponibiliteNotFoundError() {
    super("error_indisponibilite_not_exist");
  }
}