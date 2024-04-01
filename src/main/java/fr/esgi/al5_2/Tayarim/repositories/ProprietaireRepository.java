package fr.esgi.al5_2.Tayarim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.esgi.al5_2.Tayarim.entities.Proprietaire;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long>{
    
}
