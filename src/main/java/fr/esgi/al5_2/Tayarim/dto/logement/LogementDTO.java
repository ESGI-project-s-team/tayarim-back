package fr.esgi.al5_2.Tayarim.dto.logement;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class LogementDTO {

    @NonNull
    private Long id;
    @NonNull
    private ProprietaireDTO proprietaire;


}
