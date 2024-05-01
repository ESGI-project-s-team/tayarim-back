package fr.esgi.al5_2.Tayarim.exceptions;

public class ProprietaireNotFoundException extends RuntimeException {

  public ProprietaireNotFoundException() {
    super("error_owner_notExist");
  }
}