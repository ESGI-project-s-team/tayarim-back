package fr.esgi.al5.tayarim.mail;

import java.io.IOException;
import java.time.LocalDate;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

/**
 * Service pour l'envoi d'email.
 */
@Service
public class EmailService {

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
        + "\"email\":\"mailtrap@demomailtrap.com\","
        + "\"name\":\"Mailtrap Test\"},"
        + "\"to\":["
        + "{"
        + "\"email\":\"yaron1220@gmail.com\""
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
        .addHeader("Authorization", "Bearer bb1d18a1ef9e9d4089d64c2bf5d7c440")
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
   * @param nom      Le nom de l'utilisateur.
   * @param prenom   Le prénom de l'utilisateur.
   * @param numeroFacture    Le numéro de la facture.
   * @param montantFacture  Le montant de la facture.
   * @param url  L'url de la facture.
   */
  public void sendFactureEmail(String nom, String prenom, String numeroFacture,
      Float montantFacture, String url) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType,
        "{"
            + "\"from\":{"
            + "\"email\":\"mailtrap@demomailtrap.com\","
            + "\"name\":\"Mailtrap Test\""
            + "},"
            + "\"to\":["
            + "{"
            + "\"email\":\"yaron1220@gmail.com\""
            + "}"
            + "],"
            + "\"template_uuid\":\"19cd593e-41fe-4e47-b7d6-684be2c529f3\","
            + "\"template_variables\":{"
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"numeroFacture\":\"" + numeroFacture + "\","
            + "\"dateFacture\":\"" + LocalDate.now() + "\","
            + "\"montantFacture\":\"" + montantFacture.toString() + "\","
            + "\"urlFacture\":\""+ url + "\""
            + "}"
            + "}");
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer bb1d18a1ef9e9d4089d64c2bf5d7c440")
        .addHeader("Content-Type", "application/json")
        .build();
    try {
      Response response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}