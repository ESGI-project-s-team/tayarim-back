package fr.esgi.al5.tayarim.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface de contrôleur pour les opérations de base de données sur les entités Proprietaire.
 * Fournit des méthodes pour créer, lire, mettre à jour et supprimer des propriétaires.
 *
 * @param <T> Dto de retour.
 * @param <U> Dto de mise à jour.
 */
public interface UpdateMethodInterface<T, U> {

  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PutMapping("/{id}")
  ResponseEntity<T> update(@RequestAttribute("token") String authHeader, @PathVariable Long id,
      @Valid @RequestBody U u);

}
