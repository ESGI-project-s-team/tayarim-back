package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class DepenseDateInvalidError extends RuntimeException {

  public DepenseDateInvalidError() {
    super("error_expense_date_invalid");
  }
}