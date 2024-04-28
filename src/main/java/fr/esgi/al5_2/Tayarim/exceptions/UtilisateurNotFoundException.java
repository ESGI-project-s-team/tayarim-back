package fr.esgi.al5_2.Tayarim.exceptions;

public class UtilisateurNotFoundException extends RuntimeException {
    public UtilisateurNotFoundException() {
        super("error_user_notExist");
    }
}