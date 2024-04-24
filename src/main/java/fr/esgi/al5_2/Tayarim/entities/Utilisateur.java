package fr.esgi.al5_2.Tayarim.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public abstract class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name="NOM", nullable = false)
    @NonNull
    private String nom;

    @Column(name="PRENOM", nullable = false)
    @NonNull
    private String prenom;
    
    @Column(name="EMAIL", nullable = false)
    @NonNull
    private String email;

    @Column(name="NUMTEL", nullable = false)
    @NonNull
    private String numTel;

    @Column(name="MOTDEPASSE", nullable = false)
    @NonNull
    private String motDePasse;
}
