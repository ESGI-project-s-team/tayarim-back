package fr.esgi.al5.tayarim.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ReservationUpdatePaymentIntentDto {
    //@JsonProperty("paymentIntent")
    private String paymentIntent;

}

