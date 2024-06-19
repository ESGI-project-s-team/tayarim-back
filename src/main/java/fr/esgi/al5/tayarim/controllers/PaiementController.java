package fr.esgi.al5.tayarim.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import fr.esgi.al5.tayarim.dto.paiement.PaiementCancelDto;
import fr.esgi.al5.tayarim.dto.paiement.PaiementCaptureDto;
import fr.esgi.al5.tayarim.dto.paiement.PaiementDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
              .setAmount(paymentRequest.getAmount())
              .setCurrency("usd")
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
   * @param captureRequest Requête de capture.
   */
  @PostMapping("/capture-payment")
  public ResponseEntity<Void> capturePayment(@RequestBody PaiementCaptureDto captureRequest) {
    Stripe.apiKey = stripeApiKey;

    try {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(captureRequest.getPaymentIntentId());
      paymentIntent.capture();
      return ResponseEntity.ok().build();
    } catch (StripeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Annule le paiement.
   *
   * @param cancelRequest Requête d'annulation.
   */
  @PostMapping("/cancel-payment")
  public ResponseEntity<Void> cancelPayment(@RequestBody PaiementCancelDto cancelRequest) {
    Stripe.apiKey = stripeApiKey;
    try {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(cancelRequest.getPaymentIntentId());
      paymentIntent.cancel();
      return ResponseEntity.ok().build();
    } catch (StripeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}




