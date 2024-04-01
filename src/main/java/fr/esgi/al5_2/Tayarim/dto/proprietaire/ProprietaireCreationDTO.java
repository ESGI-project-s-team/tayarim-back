package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.AllArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class ProprietaireCreationDTO {

    @NonNull
    private String nom;
    @NonNull
    private String prenom;
    @NonNull
    private String email;
    @NonNull
    private String numTel;
    @NonNull
    private String motDePasse;

    // No need for explicit getters, setters, or constructors
}
