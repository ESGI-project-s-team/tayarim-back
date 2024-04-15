package fr.esgi.al5_2.Tayarim.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.esgi.al5_2.Tayarim.entities.Proprietaire;

import java.util.Optional;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long>{

    Optional<Proprietaire> findFirstByEmail(@NonNull String email);

    Optional<Proprietaire> findFirstByNumTel(@NonNull String numTel);

    Optional<Proprietaire> findFirstByEmailAndMotDePasse(@NonNull String email, @NonNull String motDePasse);


}
