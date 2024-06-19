package fr.esgi.al5.tayarim.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe de mise à jour du paymentIntent pour la réservation.
 */
@Setter
@Getter
public class ReservationUpdatePaymentIntentDto {

  @JsonProperty("paymentIntent")
  private String paymentIntent;

}

