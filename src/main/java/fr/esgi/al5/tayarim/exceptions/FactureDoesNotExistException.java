package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class FactureDoesNotExistException extends RuntimeException {

  public FactureDoesNotExistException() {
    super("error_invoice_nonexistent");
  }
}