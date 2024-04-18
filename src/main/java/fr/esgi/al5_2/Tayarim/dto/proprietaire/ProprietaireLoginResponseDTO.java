package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class ProprietaireLoginResponseDTO {

    @NonNull
    private Long id;

    @NonNull
    private String token;

    // No need for explicit getters, setters, or constructors
}
