package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import fr.esgi.al5_2.Tayarim.dto.logement.LogementDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class ProprietaireLoginResponseDTO {

    @NonNull
    private Long id;

    @NonNull
    private String token;

    // No need for explicit getters, setters, or constructors
}
