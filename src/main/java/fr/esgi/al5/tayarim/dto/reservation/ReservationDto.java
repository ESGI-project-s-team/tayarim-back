package fr.esgi.al5.tayarim.dto.reservation;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Classe pour la gestion des Dto de regles logement.
 */
@Data
@AllArgsConstructor
public class ReservationDto {

    @NonNull
    private Long id;

    @NonNull
    private String idCommande;

    @NonNull
    private String statut;

    @NonNull
    private String email;

    @NonNull
    private String nom;

    @NonNull
    private String prenom;

    @NonNull
    private Integer nbPersonnes;

    @NonNull
    private Float montant;

    @NonNull
    private LocalTime checkIn;

    @NonNull
    private LocalTime checkOut;

    @NonNull
    private Long idLogement;
}
