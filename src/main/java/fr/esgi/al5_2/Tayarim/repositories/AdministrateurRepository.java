package fr.esgi.al5_2.Tayarim.repositories;

import fr.esgi.al5_2.Tayarim.entities.Administrateur;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long>{
    Optional<Administrateur> findFirstByEmail(@NonNull String email);
    Optional<Administrateur> findFirstByNumTel(@NonNull String numTel);
}
