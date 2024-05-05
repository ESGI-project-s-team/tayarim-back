package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque la mise à jour d'un propriétaire contient des données invalides.
 */
public class ProprietaireInvalidUpdateBody extends RuntimeException {

  public ProprietaireInvalidUpdateBody() {
    super("error_owner_invalid_updateBody");
  }
}