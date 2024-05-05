package fr.esgi.al5_2.Tayarim.repositories;

import fr.esgi.al5_2.Tayarim.entities.Administrateur;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de repository pour les opérations de base de données sur les entités Administrateur.
 * Fournit des méthodes pour retrouver un administrateur par email ou par numéro de téléphone.
 */
@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {

  /**
   * Recherche le premier administrateur correspondant à l'adresse email spécifiée.
   *
   * @param email L'adresse email utilisée pour la recherche.
   * @return Un Optional contenant l'administrateur trouvé ou un Optional vide
   */
  Optional<Administrateur> findFirstByEmail(@NonNull String email);

  /**
   * Recherche le premier administrateur correspondant au numéro de téléphone spécifié.
   *
   * @param numTel Le numéro de téléphone utilisé pour la recherche.
   * @return Un Optional contenant l'administrateur trouvé ou un Optional vide
   */
  Optional<Administrateur> findFirstByNumTel(@NonNull String numTel);
}
