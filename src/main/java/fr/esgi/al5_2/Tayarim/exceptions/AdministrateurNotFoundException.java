package fr.esgi.al5_2.Tayarim.exceptions;

public class AdministrateurNotFoundException extends RuntimeException {
    public AdministrateurNotFoundException() {
        super("error_admin_notExist");
    }
}