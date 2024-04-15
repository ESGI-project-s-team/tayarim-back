package fr.esgi.al5_2.Tayarim.exceptions;

public class TokenExpireOrInvalidException extends RuntimeException {
    public TokenExpireOrInvalidException() {
        super("error_token_expire_or_invalid");
    } //TODO add to dico
}