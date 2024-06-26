package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Proprietaire;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Proprietaire.
 * Fournit des méthodes pour retrouver un propriétaire par email ou par numéro de téléphone.
 */
@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long> {

  /**
   * Recherche le premier propriétaire correspondant à l'adresse email spécifiée.
   *
   * @param email L'adresse email utilisée pour la recherche.
   * @return Un Optional contenant le propriétaire trouvé ou un Optional vide si aucun propriétaire
   *     n'est trouvé.
   */
  Optional<Proprietaire> findFirstByEmail(@NonNull String email);

  /**
   * Recherche le premier propriétaire correspondant au numéro de téléphone spécifié.
   *
   * @param numTel Le numéro de téléphone utilisé pour la recherche.
   * @return Un Optional contenant le propriétaire trouvé ou un Optional vide si aucun propriétaire
   *     n'est trouvé.
   */
  Optional<Proprietaire> findFirstByNumTel(@NonNull String numTel);
}
