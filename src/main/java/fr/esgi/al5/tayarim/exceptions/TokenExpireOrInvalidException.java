package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque le token utilisé est expiré ou invalide.
 */
public class TokenExpireOrInvalidException extends RuntimeException {

  public TokenExpireOrInvalidException() {
    super("error_token_expire_or_invalid");
  }
}