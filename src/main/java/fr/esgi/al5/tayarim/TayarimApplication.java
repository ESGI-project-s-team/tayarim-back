package fr.esgi.al5.tayarim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot pour Tayarim. Cette classe lance l'application en
 * utilisant SpringApplication, configurant automatiquement le contexte de l'application Spring basé
 * sur les annotations, les classes de configuration, et les chemins de balayage de composants.
 */
@SpringBootApplication
public class TayarimApplication {

  /**
   * Point d'entrée principal de l'application. Cette méthode lance l'application Spring Boot.
   *
   * @param args Les arguments de la ligne de commande qui peuvent être utilisés pour configurer
   *             l'application.
   */
  public static void main(String[] args) {
    SpringApplication.run(TayarimApplication.class, args);
  }

}
