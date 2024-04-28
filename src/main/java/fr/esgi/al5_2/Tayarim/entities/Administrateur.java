package fr.esgi.al5_2.Tayarim.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@PrimaryKeyJoinColumn(name = "IDUSER")
public class Administrateur extends Utilisateur{

    @Builder
    public Administrateur(@NonNull String nom, @NonNull String prenom, @NonNull String email, @NonNull String numTel, @NonNull String motDePasse) {
        // Utiliser super() pour appeler le constructeur de la classe parente
        super(nom, prenom, email, numTel, motDePasse);
    }


}
