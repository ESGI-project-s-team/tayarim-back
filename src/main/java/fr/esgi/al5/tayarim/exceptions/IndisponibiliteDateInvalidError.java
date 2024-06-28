package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class IndisponibiliteDateInvalidError extends RuntimeException {

  public IndisponibiliteDateInvalidError() {
    super("error_indisponibilite_date_invalid");
  }
}