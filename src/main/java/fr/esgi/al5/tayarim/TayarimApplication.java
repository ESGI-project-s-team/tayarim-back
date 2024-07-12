package fr.esgi.al5.tayarim;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
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
  }


}
