package fr.esgi.al5.tayarim.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface de contrôleur pour les opérations de base de données sur les entités Proprietaire.
 * Fournit des méthodes pour créer, lire, mettre à jour et supprimer des propriétaires.
 *
 * @param <T> Dto de retour.
 * @param <U> Dto de création.
 */
public interface CreateMethodInterface<T, U> {

  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PostMapping("")
  ResponseEntity<T> create(@RequestAttribute("token") String authHeader, @Valid @RequestBody U u);

}
