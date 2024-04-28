package fr.esgi.al5_2.Tayarim.repositories;

import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long>{
    Optional<Proprietaire> findFirstByEmail(@NonNull String email);
    Optional<Proprietaire> findFirstByNumTel(@NonNull String numTel);
}
