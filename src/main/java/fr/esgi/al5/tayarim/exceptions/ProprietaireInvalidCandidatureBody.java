package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ProprietaireInvalidCandidatureBody extends RuntimeException {

  public ProprietaireInvalidCandidatureBody() {
    super("error_candidate_invalid_body");
  }
}