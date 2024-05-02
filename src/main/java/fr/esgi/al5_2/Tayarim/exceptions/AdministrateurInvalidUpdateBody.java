package fr.esgi.al5_2.Tayarim.exceptions;

/**
 * Exception lancée lorsqu'une tentative de mise à jour d'un administrateur contient des données
 * invalides.
 */
public class AdministrateurInvalidUpdateBody extends RuntimeException {

  public AdministrateurInvalidUpdateBody() {
    super("error_admin_invalid_updateBody");
  }
}