package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque le token utilisé est expiré ou invalide.
 */
public class SearchDateInvalidError extends RuntimeException {

  public SearchDateInvalidError() {
    super("error_search_date_invalid");
  }
}