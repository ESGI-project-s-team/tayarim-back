package fr.esgi.al5.tayarim;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

import fr.esgi.al5.tayarim.mail.EmailService;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Classe principale de l'application Spring Boot pour Tayarim. Cette classe lance l'application en
 * utilisant SpringApplication, configurant automatiquement le contexte de l'application Spring basé
 * sur les annotations, les classes de configuration, et les chemins de balayage de composants.
 */
@SpringBootApplication
public class TayarimApplication {

  public static Bucket bucket;

  /**
   * Point d'entrée principal de l'application. Cette méthode lance l'application Spring Boot.
   *
   * @param args Les arguments de la ligne de commande qui peuvent être utilisés pour configurer
   *             l'application.
   */
  public static void main(String[] args) throws IOException {

    ApplicationContext context = SpringApplication.run(TayarimApplication.class, args);

    // Access the Environment object
    Environment env = context.getEnvironment();

    String base64Credentials = env.getProperty("GCP");

    byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
    ByteArrayInputStream credentialsStream = new ByteArrayInputStream(decodedBytes);

    GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream).createScoped(
        Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

    TayarimApplication.bucket = storage.get("tayarim-tf-storage");

    EmailService emailService = new EmailService(env);
    emailService.sendAccountConfirmationEmail("Ferreira", "Mathieu",
        "ferreira.mathieu.pro@gmail.com", "abcdefg", "fr");
    emailService.sendAccountConfirmationEmail("Ferreira", "Karl",
        "ferreira.mathieu.pro@gmail.com", "hijklm", "en");

        Storage storageFacture = TayarimApplication.bucket.getStorage();

        BlobInfo blobInfo = BlobInfo.newBuilder(TayarimApplication.bucket.getName(),
            "Factures/facture_000001.pdf").build();
    
        Blob blob = storageFacture.get(blobInfo.getBlobId()).toBuilder().setContentType("application/pdf")
            .build();

    emailService.sendFactureEmail( "ferreira.mathieu.pro@gmail.com","Ferreira", "Mathieu",
        "000001", 123f, blob, "fr");
        emailService.sendFactureEmail( "ferreira.mathieu.pro@gmail.com","Ferreira", "Karl",
        "000001", 123f, blob, "en");

    emailService.sendCreationReservationEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Mathieu", "RESA-123456", LocalDate.now().toString(), 
    "123", "https://storage.googleapis.com/tayarim-tf-storage/House%20images/1_2", "10 Fake Street, FakeCity", 
    LocalDate.now().toString(), "3", "2", 
    "fr");
    emailService.sendCreationReservationEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Karl", "RESA-123456", LocalDate.now().toString(), 
    "123", "https://storage.googleapis.com/tayarim-tf-storage/House%20images/1_2", "10 Fake Street, FakeCity", 
    LocalDate.now().toString(), "3", "2", 
    "en");

    emailService.sendAnnulationReservationEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Mathieu", "RESA-123456", LocalDate.now().toString(), 
    "123", "https://storage.googleapis.com/tayarim-tf-storage/House%20images/1_2", "10 Fake Street, FakeCity", 
    LocalDate.now().toString(), "3", "2", 
    "fr");
    emailService.sendAnnulationReservationEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Karl", "RESA-123456", LocalDate.now().toString(), 
    "123", "https://storage.googleapis.com/tayarim-tf-storage/House%20images/1_2", "10 Fake Street, FakeCity", 
    LocalDate.now().toString(), "3", "2", 
    "en");

    emailService.sendModificationReservationEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Mathieu", "RESA-123456", LocalDate.now().toString(), 
    "123", "https://storage.googleapis.com/tayarim-tf-storage/House%20images/1_2", "10 Fake Street, FakeCity", 
    LocalDate.now().toString(), "3", "2", 
    "fr");
    emailService.sendModificationReservationEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Karl", "RESA-123456", LocalDate.now().toString(), 
    "123", "https://storage.googleapis.com/tayarim-tf-storage/House%20images/1_2", "10 Fake Street, FakeCity", 
    LocalDate.now().toString(), "3", "2", 
    "en");

    emailService.sendPasswordResetEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Mathieu", "exemple.com", "fr");
    emailService.sendPasswordResetEmail("ferreira.mathieu.pro@gmail.com", "Ferreira", 
    "Karl", "exemple.com", "en");

    emailService.sendClientMessageEmail("Ceci est un message de test", "ferreira.mathieu.pro@gmail.com",
    "ferreira.mathieu.pro@gmail.com", "RESA-XXXXXX", "Ferreira", 
    "Mathieu", "fr");
    emailService.sendClientMessageEmail("Ceci est un message de test", "ferreira.mathieu.pro@gmail.com",
    "ferreira.mathieu.pro@gmail.com", "RESA-XXXXXX", "Ferreira", 
    "Mathieu", "en");
  }


}
