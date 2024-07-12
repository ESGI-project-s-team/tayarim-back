package fr.esgi.al5.tayarim.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * Interface de contrôleur pour les opérations de base de données sur les entités Proprietaire.
 * Fournit des méthodes pour créer, lire, mettre à jour et supprimer des propriétaires.
 *
 * @param <T> Dto de retour.
 */
public interface GetAllMethodInterface<T> {

  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("")
  ResponseEntity<List<T>> getAll(@RequestAttribute("token") String authHeader);

}
