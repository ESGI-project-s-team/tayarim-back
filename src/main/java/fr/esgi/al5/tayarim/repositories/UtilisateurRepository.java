package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.Utilisateur;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Proprietaire.
 * Fournit des méthodes pour retrouver un propriétaire par email ou par numéro de téléphone.
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

  Optional<Utilisateur> findByEmail(@NonNull String email);
}
