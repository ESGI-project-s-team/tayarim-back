package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

import fr.esgi.al5_2.Tayarim.dto.logement.LogementDTO;
import lombok.AllArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireDTO {

    @NonNull
    private Long id;
    @NonNull
    private String nom;
    @NonNull
    private String prenom;
    @NonNull
    private String email;
    @NonNull
    private String numTel;
    @NonNull
    private LocalDateTime dateInscription;

    private List<LogementDTO> logements;

    // No need for explicit getters, setters, or constructors
}
