package fr.esgi.al5.tayarim.mail;

import com.google.cloud.storage.Blob;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
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
      String password, String lang) {

    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    Locale locale = (lang.equals("en") ? Locale.ENGLISH : Locale.FRANCE);
    String msgHeaderValidationAccount = messageSource.getMessage("msg_header_validation_account",
        null, locale);
    String msgHello = messageSource.getMessage("msg_hello", null, locale);
    String msgAccountCreated = messageSource.getMessage("msg_account_created", null, locale);
    String msgAccountDetails = messageSource.getMessage("msg_account_details", null, locale);
    String msgDetailsMail = messageSource.getMessage("msg_details_mail", null, locale);
    String msgDetailsPass = messageSource.getMessage("msg_details_pass", null, locale);
    String msgPassReset = messageSource.getMessage("msg_pass_reset", null, locale);
    String msgHelp = messageSource.getMessage("msg_help", null, locale);
    String msgSincerely = messageSource.getMessage("msg_sincerely", null, locale);
    String msgSignatureYaronTayarim = messageSource.getMessage("msg_signature_yaron_tayarim", null,
        locale);
    String msgCopyright = messageSource.getMessage("msg_copyright", null, locale);

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
        + "\"msg_header_validation_account\":\"" + msgHeaderValidationAccount + "\","
        + "\"msg_hello\":\"" + msgHello + "\","
        + "\"nom\":\"" + nom + "\","
        + "\"prenom\":\"" + prenom + "\","
        + "\"msg_account_created\":\"" + msgAccountCreated + "\","
        + "\"msg_account_details\":\"" + msgAccountDetails + "\","
        + "\"msg_details_mail\":\"" + msgDetailsMail + "\","
        + "\"email\":\"" + email + "\","
        + "\"msg_details_pass\":\"" + msgDetailsPass + "\","
        + "\"password\":\"" + password + "\","
        + "\"msg_pass_reset\":\"" + msgPassReset + "\","
        + "\"msg_help\":\"" + msgHelp + "\","
        + "\"msg_sincerely\":\"" + msgSincerely + "\","
        + "\"msg_signature_yaron_tayarim\":\"" + msgSignatureYaronTayarim + "\","
        + "\"msg_copyright\":\"" + msgCopyright + "\""
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
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Envoi un email d'envoie de facture.
   *
   * @param nom            Le nom de l'utilisateur.
   * @param prenom         Le prénom de l'utilisateur.
   * @param numeroFacture  Le numéro de la facture.
   * @param montantFacture Le montant de la facture.
   */
  public void sendFactureEmail(String email, String nom, String prenom, String numeroFacture,
      Float montantFacture, Blob blob,
      String lang) {

    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    Locale locale = (lang.equals("en") ? Locale.ENGLISH : Locale.FRANCE);
    String msgHeaderInvoiceReceived = messageSource.getMessage("msg_header_invoice_received", null,
        locale);
    String msgHello = messageSource.getMessage("msg_hello", null, locale);
    String msgInvoiceAvailable = messageSource.getMessage("msg_invoice_available", null, locale);
    String msgInvoiceDetails = messageSource.getMessage("msg_invoice_details", null, locale);
    String msgInvoiceNumber = messageSource.getMessage("msg_invoice_number", null, locale);
    String msgInvoiceDate = messageSource.getMessage("msg_invoice_date", null, locale);
    String msgInvoiceAmount = messageSource.getMessage("msg_invoice_amount", null, locale);
    String msgHelp = messageSource.getMessage("msg_help", null, locale);
    String msgSincerely = messageSource.getMessage("msg_sincerely", null, locale);
    String msgSignatureYaronTayarim = messageSource.getMessage("msg_signature_yaron_tayarim", null,
        locale);
    String msgCopyright = messageSource.getMessage("msg_copyright", null, locale);
    OkHttpClient client = new OkHttpClient().newBuilder().build();
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
            + "\"msg_header_invoice_received\":\"" + msgHeaderInvoiceReceived + "\","
            + "\"msg_hello\":\"" + msgHello + "\","
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"msg_invoice_available\":\"" + msgInvoiceAvailable + "\","
            + "\"msg_invoice_details\":\"" + msgInvoiceDetails + "\","
            + "\"msg_invoice_number\":\"" + msgInvoiceNumber + "\","
            + "\"numeroFacture\":\"" + numeroFacture + "\","
            + "\"msg_invoice_date\":\"" + msgInvoiceDate + "\","
            + "\"dateFacture\":\"" + LocalDate.now() + "\","
            + "\"msg_invoice_amount\":\"" + msgInvoiceAmount + "\","
            + "\"montantFacture\":\"" + montantFacture.toString() + "\","
            + "\"msg_help\":\"" + msgHelp + "\","
            + "\"msg_sincerely\":\"" + msgSincerely + "\","
            + "\"msg_signature_yaron_tayarim\":\"" + msgSignatureYaronTayarim + "\","
            + "\"msg_copyright\":\"" + msgCopyright + "\""
            + "},"
            + "\"attachments\":[{"
            + "\"content\":\"" + Base64.getEncoder().encodeToString(blob.getContent()) + "\","
            + "\"filename\":\"" + blob.getName() + "\""
            + "}]"
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
      String adresseReservation, String dateArrivee, String nbNuits, String nbPersonnes,
      String lang) {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    Locale locale = (lang.equals("en") ? Locale.ENGLISH : Locale.FRANCE);
    String msgHeaderReservationConfirmation = messageSource.getMessage(
        "msg_header_reservation_confirmation", null, locale);
    String msgHello = messageSource.getMessage("msg_hello", null, locale);
    String msgReservationSuccess = messageSource.getMessage("msg_reservation_success", null,
        locale);
    String msgReservationNotice = messageSource.getMessage("msg_reservation_notice", null, locale);
    String msgReservationImageAlt = messageSource.getMessage("msg_reservation_image_alt", null,
        locale);
    String msgReservationNumber = messageSource.getMessage("msg_reservation_number", null, locale);
    String msgReservationDate = messageSource.getMessage("msg_reservation_date", null, locale);
    String msgReservationAmount = messageSource.getMessage("msg_reservation_amount", null, locale);
    String msgReservationAddress = messageSource.getMessage("msg_reservation_address", null,
        locale);
    String msgArrivalDate = messageSource.getMessage("msg_arrival_date", null, locale);
    String msgNights = messageSource.getMessage("msg_nights", null, locale);
    String msgPersons = messageSource.getMessage("msg_persons", null, locale);
    String msgHelp = messageSource.getMessage("msg_help", null, locale);
    String msgManageReservation = messageSource.getMessage("msg_manage_reservation", null, locale);
    String msgSincerely = messageSource.getMessage("msg_sincerely", null, locale);
    String msgSignatureYaronTayarim = messageSource.getMessage("msg_signature_yaron_tayarim", null,
        locale);
    String msgCopyright = messageSource.getMessage("msg_copyright", null, locale);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
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
            + "\"template_uuid\":\"6c2a21bc-2063-4552-8a02-a31ec93804fd\","
            + "\"template_variables\":{"
            + "\"msg_header_reservation_confirmation\":\"" + msgHeaderReservationConfirmation
            + "\","
            + "\"msg_hello\":\"" + msgHello + "\","
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"msg_reservation_success\":\"" + msgReservationSuccess + "\","
            + "\"msg_reservation_notice\":\"" + msgReservationNotice + "\","
            + "\"msg_reservation_image_alt\":\"" + msgReservationImageAlt + "\","
            + "\"msg_reservation_number\":\"" + msgReservationNumber + "\","
            + "\"numeroReservation\":\"" + numeroReservation + "\","
            + "\"msg_reservation_date\":\"" + msgReservationDate + "\","
            + "\"dateReservation\":\"" + dateReservation + "\","
            + "\"msg_reservation_amount\":\"" + msgReservationAmount + "\","
            + "\"montantReservation\":\"" + montantReservation + "\","
            + "\"imageReservation\":\"" + imageReservation + "\","
            + "\"msg_reservation_address\":\"" + msgReservationAddress + "\","
            + "\"adresseReservation\":\"" + adresseReservation + "\","
            + "\"msg_arrival_date\":\"" + msgArrivalDate + "\","
            + "\"dateArrivee\":\"" + dateArrivee + "\","
            + "\"msg_nights\":\"" + msgNights + "\","
            + "\"nbNuits\":\"" + nbNuits + "\","
            + "\"msg_persons\":\"" + msgPersons + "\","
            + "\"nbPersonnes\":\"" + nbPersonnes + "\","
            + "\"msg_help\":\"" + msgHelp + "\","
            + "\"url_gestion_reservation\":\"" + "https://tayarim.com/" + lang
            + "/modification-reservation" + "\","
            + "\"msg_manage_reservation\":\"" + msgManageReservation + "\","
            + "\"msg_sincerely\":\"" + msgSincerely + "\","
            + "\"msg_signature_yaron_tayarim\":\"" + msgSignatureYaronTayarim + "\","
            + "\"msg_copyright\":\"" + msgCopyright + "\""
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
      String adresseReservation, String dateArrivee, String nbNuits, String nbPersonnes,
      String lang) {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    Locale locale = (lang.equals("en") ? Locale.ENGLISH : Locale.FRANCE);
    String msgHeaderReservationCancellation = messageSource.getMessage(
        "msg_header_reservation_cancellation", null, locale);
    String msgHello = messageSource.getMessage("msg_hello", null, locale);
    String msgReservationCancelled = messageSource.getMessage("msg_reservation_cancelled", null,
        locale);
    String msgReservationImageAlt = messageSource.getMessage("msg_reservation_image_alt", null,
        locale);
    String msgReservationNumber = messageSource.getMessage("msg_reservation_number", null, locale);
    String msgReservationDate = messageSource.getMessage("msg_reservation_date", null, locale);
    String msgReservationAmount = messageSource.getMessage("msg_reservation_amount", null, locale);
    String msgReservationAddress = messageSource.getMessage("msg_reservation_address", null,
        locale);
    String msgArrivalDate = messageSource.getMessage("msg_arrival_date", null, locale);
    String msgNights = messageSource.getMessage("msg_nights", null, locale);
    String msgPersons = messageSource.getMessage("msg_persons", null, locale);
    String msgHelp = messageSource.getMessage("msg_help", null, locale);
    String msgSincerely = messageSource.getMessage("msg_sincerely", null, locale);
    String msgSignatureYaronTayarim = messageSource.getMessage("msg_signature_yaron_tayarim", null,
        locale);
    String msgCopyright = messageSource.getMessage("msg_copyright", null, locale);
    String msgManageReservation = messageSource.getMessage("msg_manage_reservation", null, locale);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
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
            + "\"template_uuid\":\"41467c3f-c6cb-4cb1-a778-630197e23922\","
            + "\"template_variables\":{"
            + "\"msg_header_reservation_cancellation\":\"" + msgHeaderReservationCancellation
            + "\","
            + "\"msg_hello\":\"" + msgHello + "\","
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"msg_reservation_cancelled\":\"" + msgReservationCancelled + "\","
            + "\"msg_reservation_image_alt\":\"" + msgReservationImageAlt + "\","
            + "\"msg_reservation_number\":\"" + msgReservationNumber + "\","
            + "\"numeroReservation\":\"" + numeroReservation + "\","
            + "\"msg_reservation_date\":\"" + msgReservationDate + "\","
            + "\"dateReservation\":\"" + dateReservation + "\","
            + "\"msg_reservation_amount\":\"" + msgReservationAmount + "\","
            + "\"montantReservation\":\"" + montantReservation + "\","
            + "\"imageReservation\":\"" + imageReservation + "\","
            + "\"msg_reservation_address\":\"" + msgReservationAddress + "\","
            + "\"adresseReservation\":\"" + adresseReservation + "\","
            + "\"msg_arrival_date\":\"" + msgArrivalDate + "\","
            + "\"dateArrivee\":\"" + dateArrivee + "\","
            + "\"msg_nights\":\"" + msgNights + "\","
            + "\"nbNuits\":\"" + nbNuits + "\","
            + "\"msg_persons\":\"" + msgPersons + "\","
            + "\"nbPersonnes\":\"" + nbPersonnes + "\","
            + "\"msg_help\":\"" + msgHelp + "\","
            + "\"msg_manage_reservation\":\"" + msgManageReservation + "\","
            + "\"url_gestion_reservation\":\"" + "https://tayarim.com/" + lang
            + "/modification-reservation" + "\","
            + "\"msg_sincerely\":\"" + msgSincerely + "\","
            + "\"msg_signature_yaron_tayarim\":\"" + msgSignatureYaronTayarim + "\","
            + "\"msg_copyright\":\"" + msgCopyright + "\""
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
      String adresseReservation, String dateArrivee, String nbNuits, String nbPersonnes,
      String lang) {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    Locale locale = (lang.equals("en") ? Locale.ENGLISH : Locale.FRANCE);
    String msgHeaderReservationModification = messageSource.getMessage(
        "msg_header_reservation_modification", null, locale);
    String msgHello = messageSource.getMessage("msg_hello", null, locale);
    String msgReservationModified = messageSource.getMessage("msg_reservation_modified", null,
        locale);
    String msgReservationImageAlt = messageSource.getMessage("msg_reservation_image_alt", null,
        locale);
    String msgReservationNumber = messageSource.getMessage("msg_reservation_number", null, locale);
    String msgReservationDate = messageSource.getMessage("msg_reservation_date", null, locale);
    String msgReservationAmount = messageSource.getMessage("msg_reservation_amount", null, locale);
    String msgReservationAddress = messageSource.getMessage("msg_reservation_address", null,
        locale);
    String msgArrivalDate = messageSource.getMessage("msg_arrival_date", null, locale);
    String msgNights = messageSource.getMessage("msg_nights", null, locale);
    String msgPersons = messageSource.getMessage("msg_persons", null, locale);
    String msgHelp = messageSource.getMessage("msg_help", null, locale);
    String msgManageReservation = messageSource.getMessage("msg_manage_reservation", null, locale);
    String msgSincerely = messageSource.getMessage("msg_sincerely", null, locale);
    String msgSignatureYaronTayarim = messageSource.getMessage("msg_signature_yaron_tayarim", null,
        locale);
    String msgCopyright = messageSource.getMessage("msg_copyright", null, locale);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
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
            + "\"template_uuid\":\"50e0db32-33a5-405f-ba65-0f6f4a9fec4d\","
            + "\"template_variables\":{"
            + "\"msg_header_reservation_modification\":\"" + msgHeaderReservationModification
            + "\","
            + "\"msg_hello\":\"" + msgHello + "\","
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"msg_reservation_modified\":\"" + msgReservationModified + "\","
            + "\"msg_reservation_image_alt\":\"" + msgReservationImageAlt + "\","
            + "\"msg_reservation_number\":\"" + msgReservationNumber + "\","
            + "\"numeroReservation\":\"" + numeroReservation + "\","
            + "\"msg_reservation_date\":\"" + msgReservationDate + "\","
            + "\"dateReservation\":\"" + dateReservation + "\","
            + "\"msg_reservation_amount\":\"" + msgReservationAmount + "\","
            + "\"montantReservation\":\"" + montantReservation + "\","
            + "\"imageReservation\":\"" + imageReservation + "\","
            + "\"msg_reservation_address\":\"" + msgReservationAddress + "\","
            + "\"adresseReservation\":\"" + adresseReservation + "\","
            + "\"msg_arrival_date\":\"" + msgArrivalDate + "\","
            + "\"dateArrivee\":\"" + dateArrivee + "\","
            + "\"msg_nights\":\"" + msgNights + "\","
            + "\"nbNuits\":\"" + nbNuits + "\","
            + "\"msg_persons\":\"" + msgPersons + "\","
            + "\"nbPersonnes\":\"" + nbPersonnes + "\","
            + "\"msg_help\":\"" + msgHelp + "\","
            + "\"url_gestion_reservation\":\"" + "https://tayarim.com/" + lang
            + "/modification-reservation" + "\","
            + "\"msg_manage_reservation\":\"" + msgManageReservation + "\","
            + "\"msg_sincerely\":\"" + msgSincerely + "\","
            + "\"msg_signature_yaron_tayarim\":\"" + msgSignatureYaronTayarim + "\","
            + "\"msg_copyright\":\"" + msgCopyright + "\""
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
   * @param token                Le token de réinitialisation de mot de passe.
   */
  public void sendPasswordResetEmail(String email, String nom, String prenom,
      String token, String lang) {

    String lienReinitialisation = "https://tayarim.com/" + lang + "/recover?token=" + token;

    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    Locale locale = (lang.equals("en") ? Locale.ENGLISH : Locale.FRANCE);
    String msgHeaderPasswordReset = messageSource.getMessage("msg_header_password_reset", null,
        locale);
    String msgHello = messageSource.getMessage("msg_hello", null, locale);
    String msgPasswordResetRequest = messageSource.getMessage("msg_password_reset_request", null,
        locale);
    String msgResetPasswordLink = messageSource.getMessage("msg_reset_password_link", null, locale);
    String msgPasswordResetIgnore = messageSource.getMessage("msg_password_reset_ignore", null,
        locale);
    String msgHelp = messageSource.getMessage("msg_help", null, locale);
    String msgSincerely = messageSource.getMessage("msg_sincerely", null, locale);
    String msgSignatureYaronTayarim = messageSource.getMessage("msg_signature_yaron_tayarim", null,
        locale);
    String msgCopyright = messageSource.getMessage("msg_copyright", null, locale);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
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
            + "\"template_uuid\":\"1e166c1e-4b7a-4aa0-8844-77f81e51b0aa\","
            + "\"template_variables\":{"
            + "\"msg_header_password_reset\":\"" + msgHeaderPasswordReset + "\","
            + "\"msg_hello\":\"" + msgHello + "\","
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"msg_password_reset_request\":\"" + msgPasswordResetRequest + "\","
            + "\"lienReinitialisation\":\"" + lienReinitialisation + "\","
            + "\"msg_reset_password_link\":\"" + msgResetPasswordLink + "\","
            + "\"msg_password_reset_ignore\":\"" + msgPasswordResetIgnore + "\","
            + "\"msg_help\":\"" + msgHelp + "\","
            + "\"msg_sincerely\":\"" + msgSincerely + "\","
            + "\"msg_signature_yaron_tayarim\":\"" + msgSignatureYaronTayarim + "\","
            + "\"msg_copyright\":\"" + msgCopyright + "\""
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
   * Envoi le message du client.
   */
  public void sendClientMessageEmail(String message, String email, String emailClient,
      String numeroResa, String nom,
      String prenom, String lang) {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    Locale locale = (lang.equals("en") ? Locale.ENGLISH : Locale.FRANCE);
    String msgHeaderClientMessage = messageSource.getMessage("msg_header_client_message", null,
        locale);
    String msgClientMessageIntro = messageSource.getMessage("msg_client_message_intro", null,
        locale);
    String msgClientMessageReservation = messageSource.getMessage("msg_client_message_reservation",
        null, locale);
    String msgClientMessageLabel = messageSource.getMessage("msg_client_message_label", null,
        locale);
    String msgCopyright = messageSource.getMessage("msg_copyright", null, locale);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType,
        "{"
            + "\"from\":{"
            + "\"email\":\"mailtrap@tayarim.com\","
            + "\"name\":\"Tayarim\""
            + "},"
            + "\"to\":[{"
            + "\"email\":\"" + email + "\""
            + "}],"
            + "\"template_uuid\":\"667cbf99-8f38-451f-94d6-e86cb51dfaa8\","
            + "\"template_variables\":{"
            + "\"msg_header_client_message\":\"" + msgHeaderClientMessage + "\","
            + "\"numeroResa\":\"" + numeroResa + "\","
            + "\"nom\":\"" + nom + "\","
            + "\"prenom\":\"" + prenom + "\","
            + "\"email\":\"" + emailClient + "\","
            + "\"msg_client_message_intro\":\"" + msgClientMessageIntro + "\","
            + "\"msg_client_message_reservation\":\"" + msgClientMessageReservation + "\","
            + "\"msg_client_message_label\":\"" + msgClientMessageLabel + "\","
            + "\"message\":\"" + message + "\","
            + "\"msg_copyright\":\"" + msgCopyright + "\""
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