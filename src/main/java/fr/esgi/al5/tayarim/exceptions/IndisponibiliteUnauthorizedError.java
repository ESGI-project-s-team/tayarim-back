package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class IndisponibiliteUnauthorizedError extends RuntimeException {

  public IndisponibiliteUnauthorizedError() {
    super("error_indisponibilite_unauthorized");
  }
}