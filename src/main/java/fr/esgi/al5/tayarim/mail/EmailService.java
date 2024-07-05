package fr.esgi.al5.tayarim.mail;

import java.io.IOException;
import java.time.LocalDate;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Service pour l'envoi d'email.
 */
@Service
public class EmailService {

  private final Environment env;

  public EmailService(Environment env) {
    this.env = env;
  }

  /**
   * Envoi un email de confirmation de compte.
   *
   * @param nom      Le nom de l'utilisateur.
   * @param prenom   Le prénom de l'utilisateur.
   * @param email    L'email de l'utilisateur.
   * @param password Le mot de passe de l'utilisateur.
   */
  public void sendAccountConfirmationEmail(String nom, String prenom, String email,
      String password) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    String bodyString = "{"
        + "\"from\":{"
        + "\"email\":\"mailtrap@tayarim.com\","
        + "\"name\":\"Tayarim\"},"
        + "\"to\":["
        + "{"
        + "\"email\":\"" + email + "\""
        + "}"
        + "],"
        + "\"template_uuid\":\"282f9283-852d-46ac-9609-2bd368bd208f\","
        + "\"template_variables\":{"
        + "\"nom\":\"" + nom + "\","
        + "\"prenom\":\"" + prenom + "\","
        + "\"email\":\"" + email + "\","
        + "\"password\":\"" + password + "\""
        + "}"
        + "}";
    RequestBody body = RequestBody.create(mediaType,
        bodyString);
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer " + env.getProperty("MAILTRAP_API_KEY"))
        .addHeader("Content-Type", "application/json")
        .build();
    try {
      Response response = client.newCall(request).execute();
      System.out.println(response.body().string());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Envoi un email de réinitialisation de mot de passe.
   *
   * @param nom            Le nom de l'utilisateur.
   * @param prenom         Le prénom de l'utilisateur.
   * @param numeroFacture  Le numéro de la facture.
   * @param montantFacture Le montant de la facture.
   * @param url            L'url de la facture.
   */
  public void sendFactureEmail(String email, String nom, String prenom, String numeroFacture,
      Float montantFacture, String url) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType,
        "{"
            + "\"from\":{"
            + "\"email\":\"mailtrap@tayarim.com\","
            + "\"name\":\"Tayarim\""
            + "},"
            + "\"to\":["
            + "{"
            + "\"email\":\"" + email + "\""
            + "}"
            + "],"
            + "\"template_uuid\":\"19cd593e-41fe-4e47-b7d6-684be2c529f3\","
            + "\"template_variables\":{"
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"numeroFacture\":\"" + numeroFacture + "\","
            + "\"dateFacture\":\"" + LocalDate.now() + "\","
            + "\"montantFacture\":\"" + montantFacture.toString() + "\","
            + "\"urlFacture\":\"" + url + "\""
            + "}"
            + "}");
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer " + env.getProperty("MAILTRAP_API_KEY"))
        .addHeader("Content-Type", "application/json")
        .build();
    try {
      Response response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Envoi un email de création de réservation.
   *
   * @param nom                Le nom de l'utilisateur.
   * @param prenom             Le prénom de l'utilisateur.
   * @param numeroReservation  Le numéro de la réservation.
   * @param dateReservation    La date de la réservation.
   * @param montantReservation Le montant de la réservation.
   * @param imageReservation   L'image de la réservation.
   * @param adresseReservation L'adresse de la réservation.
   * @param dateArrivee        La date d'arrivée.
   * @param nbNuits            Le nombre de nuits.
   * @param nbPersonnes        Le nombre de personnes.
   */
  public void sendCreationReservationEmail(String email, String nom, String prenom,
      String numeroReservation,
      String dateReservation, String montantReservation, String imageReservation,
      String adresseReservation, String dateArrivee, String nbNuits, String nbPersonnes) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{"
        + "\"from\":{"
        + "\"email\":\"mailtrap@tayarim.com\","
        + "\"name\":\"Tayarim\"},"
        + "\"to\":["
        + "{"
        + "\"email\":\"" + email + "\""
        + "}"
        + "],"
        + "\"template_uuid\":\"6c2a21bc-2063-4552-8a02-a31ec93804fd\","
        + "\"template_variables\":{"
        + "\"nom\":\"" + nom + "\","
        + "\"prenom\":\"" + prenom + "\","
        + "\"numeroReservation\":\"" + numeroReservation + "\","
        + "\"dateReservation\":\"" + dateReservation + "\","
        + "\"montantReservation\":\"" + montantReservation + "\","
        + "\"imageReservation\":\"" + imageReservation + "\","
        + "\"adresseReservation\":\"" + adresseReservation + "\","
        + "\"dateArrivee\":\"" + dateArrivee + "\","
        + "\"nbNuits\":\"" + nbNuits + "\","
        + "\"nbPersonnes\":\"" + nbPersonnes + "\""
        + "}"
        + "}");
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer " + env.getProperty("MAILTRAP_API_KEY"))
        .addHeader("Content-Type", "application/json")
        .build();
    try {
      Response response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Envoi un email d'annulation de réservation.
   *
   * @param nom                Le nom de l'utilisateur.
   * @param prenom             Le prénom de l'utilisateur.
   * @param numeroReservation  Le numéro de la réservation.
   * @param dateReservation    La date de la réservation.
   * @param montantReservation Le montant de la réservation.
   * @param imageReservation   L'image de la réservation.
   * @param adresseReservation L'adresse de la réservation.
   * @param dateArrivee        La date d'arrivée.
   * @param nbNuits            Le nombre de nuits.
   * @param nbPersonnes        Le nombre de personnes.
   */
  public void sendAnnulationReservationEmail(String email, String nom, String prenom,
      String numeroReservation,
      String dateReservation, String montantReservation, String imageReservation,
      String adresseReservation, String dateArrivee, String nbNuits, String nbPersonnes) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{"
        + "\"from\":{"
        + "\"email\":\"mailtrap@tayarim.com\","
        + "\"name\":\"Tayarim\"},"
        + "\"to\":["
        + "{"
        + "\"email\":\"" + email + "\""
        + "}"
        + "],"
        + "\"template_uuid\":\"41467c3f-c6cb-4cb1-a778-630197e23922\","
        + "\"template_variables\":{"
        + "\"nom\":\"" + nom + "\","
        + "\"prenom\":\"" + prenom + "\","
        + "\"numeroReservation\":\"" + numeroReservation + "\","
        + "\"dateReservation\":\"" + dateReservation + "\","
        + "\"montantReservation\":\"" + montantReservation + "\","
        + "\"imageReservation\":\"" + imageReservation + "\","
        + "\"adresseReservation\":\"" + adresseReservation + "\","
        + "\"dateArrivee\":\"" + dateArrivee + "\","
        + "\"nbNuits\":\"" + nbNuits + "\","
        + "\"nbPersonnes\":\"" + nbPersonnes + "\""
        + "}"
        + "}");
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer " + env.getProperty("MAILTRAP_API_KEY"))
        .addHeader("Content-Type", "application/json")
        .build();
    try {
      Response response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Envoi un email de modification de réservation.
   *
   * @param nom                Le nom de l'utilisateur.
   * @param prenom             Le prénom de l'utilisateur.
   * @param numeroReservation  Le numéro de la réservation.
   * @param dateReservation    La date de la réservation.
   * @param montantReservation Le montant de la réservation.
   * @param imageReservation   L'image de la réservation.
   * @param adresseReservation L'adresse de la réservation.
   * @param dateArrivee        La date d'arrivée.
   * @param nbNuits            Le nombre de nuits.
   * @param nbPersonnes        Le nombre de personnes.
   */
  public void sendModificationReservationEmail(String email, String nom, String prenom,
      String numeroReservation,
      String dateReservation, String montantReservation, String imageReservation,
      String adresseReservation, String dateArrivee, String nbNuits, String nbPersonnes) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{"
        + "\"from\":{"
        + "\"email\":\"mailtrap@tayarim.com\","
        + "\"name\":\"Tayarim\"},"
        + "\"to\":["
        + "{"
        + "\"email\":\"" + email + "\""
        + "}"
        + "],"
        + "\"template_uuid\":\"50e0db32-33a5-405f-ba65-0f6f4a9fec4d\","
        + "\"template_variables\":{"
        + "\"nom\":\"" + nom + "\","
        + "\"prenom\":\"" + prenom + "\","
        + "\"numeroReservation\":\"" + numeroReservation + "\","
        + "\"dateReservation\":\"" + dateReservation + "\","
        + "\"montantReservation\":\"" + montantReservation + "\","
        + "\"imageReservation\":\"" + imageReservation + "\","
        + "\"adresseReservation\":\"" + adresseReservation + "\","
        + "\"dateArrivee\":\"" + dateArrivee + "\","
        + "\"nbNuits\":\"" + nbNuits + "\","
        + "\"nbPersonnes\":\"" + nbPersonnes + "\""
        + "}"
        + "}");
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer " + env.getProperty("MAILTRAP_API_KEY"))
        .addHeader("Content-Type", "application/json")
        .build();
    try {
      Response response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Envoi un email de réinitialisation de mot de passe.
   *
   * @param nom                  Le nom de l'utilisateur.
   * @param prenom               Le prénom de l'utilisateur.
   * @param lienReinitialisation Le lien de réinitialisation de mot de passe.
   */
  public void sendPasswordResetEmail(String email, String nom, String prenom,
      String lienReinitialisation) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{"
        + "\"from\":{"
        + "\"email\":\"mailtrap@tayarim.com\","
        + "\"name\":\"Tayarim\"},"
        + "\"to\":["
        + "{"
        + "\"email\":\"" + email + "\""
        + "}"
        + "],"
        + "\"template_uuid\":\"1e166c1e-4b7a-4aa0-8844-77f81e51b0aa\","
        + "\"template_variables\":{"
        + "\"nom\":\"" + nom + "\","
        + "\"prenom\":\"" + prenom + "\","
        + "\"lienReinitialisation\":\"" + lienReinitialisation + "\""
        + "}"
        + "}");
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer " + env.getProperty("MAILTRAP_API_KEY"))
        .addHeader("Content-Type", "application/json")
        .build();
    try {
      Response response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}