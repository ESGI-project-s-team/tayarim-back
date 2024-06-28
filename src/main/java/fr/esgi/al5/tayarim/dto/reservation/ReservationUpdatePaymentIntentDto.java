package fr.esgi.al5.tayarim.dto.reservation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO pour la mise à jour de l'identifiant du paiement d'une réservation.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ReservationUpdatePaymentIntentDto {

  private String paymentIntent;

}

