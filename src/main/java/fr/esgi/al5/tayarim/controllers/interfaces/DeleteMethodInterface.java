package fr.esgi.al5.tayarim.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * Interface de contrôleur pour les opérations de base de données sur les entités Proprietaire.
 * Fournit des méthodes pour créer, lire, mettre à jour et supprimer des propriétaires.
 *
 * @param <T> Dto de retour.
 */
public interface DeleteMethodInterface<T> {

  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @DeleteMapping("/{id}")
  ResponseEntity<T> delete(@RequestAttribute("token") String authHeader, @PathVariable Long id);

}
