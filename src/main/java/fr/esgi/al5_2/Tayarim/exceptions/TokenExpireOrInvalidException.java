package fr.esgi.al5_2.Tayarim.exceptions;

/**
 * Exception lancée lorsque le token utilisé est expiré ou invalide.
 */
public class TokenExpireOrInvalidException extends RuntimeException {

  public TokenExpireOrInvalidException() {
    super("error_token_expire_or_invalid");
  }
}