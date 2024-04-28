package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class AdministrateurUpdateDTO {

    private String nom;
    private String prenom;
    private String email;
    private String numTel;
    private String motDePasse;

    // No need for explicit getters, setters, or constructors
}
