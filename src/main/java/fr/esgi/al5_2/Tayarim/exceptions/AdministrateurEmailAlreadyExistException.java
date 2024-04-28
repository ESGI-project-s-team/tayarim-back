package fr.esgi.al5_2.Tayarim.exceptions;

public class AdministrateurEmailAlreadyExistException extends RuntimeException {
    public AdministrateurEmailAlreadyExistException() {
        super("error_admin_alreadyExist_email");
    }
}