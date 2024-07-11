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
  private String numTel;

  @NonNull
  private String nom;

  @NonNull
  private String prenom;

  @NonNull
  private Integer nbPersonnes;

  @NonNull
  private Float montant;

  @NonNull
  private String checkIn;

  @NonNull
  private String checkOut;

  @NonNull
  private String dateArrivee;

  @NonNull
  private String dateDepart;

  @NonNull
  private Long idLogement;

  @NonNull
  private String titreLogement;

  @NonNull
  private String dateReservation;

  private String paymentIntent;

  @NonNull
  private String lang;
}
