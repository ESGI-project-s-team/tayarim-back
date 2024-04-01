package fr.esgi.al5_2.Tayarim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RequiredArgsConstructor
//on donne les 3 constructor pour exclure Id. Moi non plus je comprend pas comment c'est possible
@MappedSuperclass
public abstract class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
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
    private String MotDePasse;
}
