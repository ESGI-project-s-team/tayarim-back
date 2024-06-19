package fr.esgi.al5.tayarim.controllers.interfaces;

import fr.esgi.al5.tayarim.exceptions.AdministrateurEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.AdministrateurInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteDateInvalidError;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteLogementNotFoundError;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteUnauthorizedError;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidAmenagement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidCreationBody;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidReglesLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidTypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.PasswordHashNotPossibleException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.ReservationDateConflictError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateInvalideError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateTooShortError;
import fr.esgi.al5.tayarim.exceptions.ReservationNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationPeopleCapacityError;
import fr.esgi.al5.tayarim.exceptions.ReservationStatusUpdateError;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UnauthorizedException;
import fr.esgi.al5.tayarim.exceptions.UnsupportedMethodPathException;
import fr.esgi.al5.tayarim.exceptions.UtilisateurNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Interface pour les contrôleurs contenant des méthodes utilitaires.
 */
public interface ControllerUtils {

  /**
   * Gère les exceptions liées aux arguments de méthode non valides en retournant un message
   * d'erreur approprié.
   *
   * @param ex L'exception capturée de type MethodArgumentNotValidException.
   * @return Une carte associant le type d'erreur à la liste de messages d'erreur.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  default Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, List<String>> errorMapping = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String errorMessage = error.getDefaultMessage();
      if (!errors.contains(errorMessage)) {
        errors.add(errorMessage);
      }
    });

    errorMapping.put("errors", errors);

    return errorMapping;
  }

  /**
   * Gère les exceptions lorsque le corps de mise à jour de l'administrateur est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UnsupportedMethodPathException.class)
  default Map<String, List<String>> unsupportedMethodPathException(
      UnsupportedMethodPathException ex) {
    return mapException(ex);
  }


  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({IndisponibiliteLogementNotFoundError.class})
  default Map<String, List<String>> indisponibiliteLogementNotFoundError(
      IndisponibiliteLogementNotFoundError ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({IndisponibiliteDateInvalidError.class})
  default Map<String, List<String>> indisponibiliteDateInvalidError(
      IndisponibiliteDateInvalidError ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({IndisponibiliteUnauthorizedError.class})
  default Map<String, List<String>> indisponibiliteUnauthorizedError(
      IndisponibiliteUnauthorizedError ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ReservationNotFoundException.class})
  default Map<String, List<String>> reservationNotFoundException(ReservationNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ReservationStatusUpdateError.class})
  default Map<String, List<String>> reservationStatusUpdateError(ReservationStatusUpdateError ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ReservationDateConflictError.class})
  default Map<String, List<String>> reservationDateConflictError(ReservationDateConflictError ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ReservationDateInvalideError.class})
  default Map<String, List<String>> reservationDateInvalideError(ReservationDateInvalideError ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ReservationDateTooShortError.class})
  default Map<String, List<String>> reservationDateTooShortError(ReservationDateTooShortError ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ReservationPeopleCapacityError.class})
  default Map<String, List<String>> reservationPeopleCapacityError(
      ReservationPeopleCapacityError ex) {
    return mapException(ex);
  }


  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidUpdateBody.class})
  default Map<String, List<String>> logementInvalidUpdateBody(LogementInvalidUpdateBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidTypeLogement.class})
  default Map<String, List<String>> logementInvalidTypeLogement(LogementInvalidTypeLogement ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidReglesLogement.class})
  default Map<String, List<String>> logementInvalidReglesLogement(
      LogementInvalidReglesLogement ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidAmenagement.class})
  default Map<String, List<String>> logementInvalidAmenagement(LogementInvalidAmenagement ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidCreationBody.class})
  default Map<String, List<String>> logementInvalidCreationBody(LogementInvalidCreationBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({LogementNotFoundException.class})
  default Map<String, List<String>> logementNotFoundException(LogementNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque l'utilisateur n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({UtilisateurNotFoundException.class})
  default Map<String, List<String>> utilisateurNotFoundException(UtilisateurNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le proprietaire n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ProprietaireNotFoundException.class})
  default Map<String, List<String>> proprietaireNotFoundException(
      ProprietaireNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update du proprietaire est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ProprietaireInvalidUpdateBody.class})
  default Map<String, List<String>> proprietaireInvalidUpdateBody(
      ProprietaireInvalidUpdateBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque un proprietaire avec le même mail existe déjà.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ProprietaireEmailAlreadyExistException.class})
  default Map<String, List<String>> proprietaireEmailAlreadyExistException(
      ProprietaireEmailAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque un proprietaire avec le même numéro de téléphone existe déjà.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ProprietaireNumTelAlreadyExistException.class})
  default Map<String, List<String>> proprietaireNumTelAlreadyExistException(
      ProprietaireNumTelAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque l'administrateur n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AdministrateurNotFoundException.class})
  default Map<String, List<String>> administrateurNotFoundException(
      AdministrateurNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque un administrateur avec le même mail existe déjà.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AdministrateurEmailAlreadyExistException.class})
  default Map<String, List<String>> administrateurEmailAlreadyExistException(
      AdministrateurEmailAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque un administrateur avec le même numéro de téléphone existe déjà.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AdministrateurNumTelAlreadyExistException.class})
  default Map<String, List<String>> administrateurNumTelAlreadyExistException(
      AdministrateurNumTelAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update de l'administrateur est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AdministrateurInvalidUpdateBody.class})
  default Map<String, List<String>> administrateurInvalidUpdateBody(
      AdministrateurInvalidUpdateBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le token est expiré ou invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({TokenExpireOrInvalidException.class})
  default Map<String, List<String>> tokenExpireOrInvalidException(
      TokenExpireOrInvalidException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le hashage du mot de passe n'est pas possible.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({PasswordHashNotPossibleException.class})
  default Map<String, List<String>> passwordHashNotPossibleException(
      PasswordHashNotPossibleException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque l'utilisateur n'est pas autorisé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({UnauthorizedException.class})
  default Map<String, List<String>> unauthorizedException(UnauthorizedException ex) {
    return mapException(ex);
  }

  /**
   * Crée une carte d'erreurs à partir d'une exception runtime.
   *
   * @param exception L'exception à mapper.
   * @return Une carte des erreurs associées à l'exception.
   */
  private Map<String, List<String>> mapException(RuntimeException exception) {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, List<String>> errorMapping = new HashMap<>();
    errors.add(exception.getMessage());

    errorMapping.put("errors", errors);

    return errorMapping;
  }

  /**
   * Extrait le token JWT de l'en-tête d'autorisation.
   *
   * @param authHeader L'en-tête contenant le token.
   * @return Le token extrait.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  default String getTokenFromHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new TokenExpireOrInvalidException();
    }

    return authHeader.substring(7);
  }

}
