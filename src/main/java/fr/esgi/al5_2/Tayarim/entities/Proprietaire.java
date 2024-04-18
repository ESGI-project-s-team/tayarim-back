package fr.esgi.al5_2.Tayarim.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Entity
public class Proprietaire extends Utilisateur{

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proprietaire")
    private List<Logement> logements;

    @Column(name="DATEINSCRIPTION", nullable = false)
    @NonNull
    private LocalDateTime dateInscription;

    public Proprietaire(@NonNull String nom, @NonNull String prenom, @NonNull String email, @NonNull String numTel, @NonNull String motDePasse, @NonNull LocalDateTime dateInscription) {
        // Utiliser super() pour appeler le constructeur de la classe parente
        super(nom, prenom, email, numTel, motDePasse); 
    
        // Initialisation des champs propres à Proprietaire
        this.dateInscription = dateInscription;
        // Initialisation de la liste des logements pour éviter NullPointerException lors de l'ajout de logements
        this.logements = List.of();
    }
    

}
