package fr.esgi.al5_2.Tayarim.exceptions;

public class ProprietaireEmailAlreadyExistException extends RuntimeException {

  public ProprietaireEmailAlreadyExistException() {
    super("error_owner_alreadyExist_email");
  }
}