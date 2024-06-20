package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque le token utilisé est expiré ou invalide.
 */
public class SearchDateMissingError extends RuntimeException {

  public SearchDateMissingError() {
    super("error_search_date_missing");
  }
}