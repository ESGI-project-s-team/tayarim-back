package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class IndisponibiliteLogementNotFoundError extends RuntimeException {

  public IndisponibiliteLogementNotFoundError() {
    super("error_indisponibilite_home_not_found");
  }
}