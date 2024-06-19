package fr.esgi.al5.tayarim.dto.paiement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DTO pour l'annulation d'un paiement. Contient l'identifiant du paiement à annuler.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class PaiementCaptureDto {

  private String paymentIntentId;
}
