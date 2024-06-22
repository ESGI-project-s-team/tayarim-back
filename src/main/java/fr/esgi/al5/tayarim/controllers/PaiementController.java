package fr.esgi.al5.tayarim.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import fr.esgi.al5.tayarim.dto.paiement.PaiementDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.ReservationNotFoundException;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.services.ReservationService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Contrôleur pour la gestion des paiements.
 */
@RestController
@PropertySource("classpath:application.properties")
@RequestMapping("/api/payment")
public class PaiementController {

    /**
     * Clé secrète de l'API Stripe.
     */
    @Value("${stripe.secret-key}")
    private String stripeApiKey;

    private final ReservationService reservationService;

    /**
     * Construit le contrôleur avec le service de réservation nécessaire.
     *
     * @param reservationService Le service de réservation.
     */
    public PaiementController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Crée un paiement.
     *
     * @param paymentRequest Requête de paiement.
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @RequestBody PaiementDto paymentRequest) {
        Stripe.apiKey = stripeApiKey;

        try {
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(paymentRequest.getAmount() * 100)
                            .setCurrency("eur")
                            .setCaptureMethod(
                                    PaymentIntentCreateParams.CaptureMethod.MANUAL) // Ensuring manual capture method
                            .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Capture le paiement.
     *
     * @param id id de la résearvation à capturer.
     */
    @PutMapping("/capture-payment/{id}")
    public ResponseEntity<ReservationDto> capturePayment(@NonNull @PathVariable Long id) {
        Stripe.apiKey = stripeApiKey;

        return new ResponseEntity<>(
                reservationService.validate(id),
                HttpStatus.OK
        );

    }

}




