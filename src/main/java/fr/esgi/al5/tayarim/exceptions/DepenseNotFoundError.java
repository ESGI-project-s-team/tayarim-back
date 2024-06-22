package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class DepenseNotFoundError extends RuntimeException {

  public DepenseNotFoundError() {
    super("error_expense_not_exists");
  }
}