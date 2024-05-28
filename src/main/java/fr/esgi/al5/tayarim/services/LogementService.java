package fr.esgi.al5.tayarim.services;


import fr.esgi.al5.tayarim.auth.JwtHelper;
import fr.esgi.al5.tayarim.auth.TokenCacheService;
import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.dto.auth.AuthLoginResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthRefreshResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthResponseDto;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UtilisateurNotFoundException;
import fr.esgi.al5.tayarim.mappers.LogementMapper;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les logements.
 */
@Service
@Transactional(readOnly = true)
public class LogementService {

  private final LogementRepository logementRepository;
  private final ProprietaireRepository proprietaireRepository;


  /**
   * Constructeur pour le service de logement.
   *
   * @param logementRepository Le repository des logements.
   */
  public LogementService(LogementRepository logementRepository,
      ProprietaireRepository proprietaireRepository) {
    this.logementRepository = logementRepository;
    this.proprietaireRepository = proprietaireRepository;
  }

  /**
   * Tente de créer un logement.
   *
   * @param logementCreationDto Le dto de création de logement.
   * @return {@link LogementDto}
   */
  @Transactional
  public LogementDto createLogement(@NonNull LogementCreationDto logementCreationDto) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(
        logementCreationDto.getIdProprietaire());

    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    Proprietaire proprietaire = optionalProprietaire.get();

    String[] parts = logementCreationDto.getUrl().split("/");

    String roomId = null;
    for (int i = 0; i < parts.length; i++) {
      if (parts[i].equals("rooms")) {
        roomId = parts[i + 1].split("\\?")[0];
        break;
      }
    }

    if (roomId != null) {
      System.out.println("ID de la chambre : " + roomId);
    } else {
      System.out.println("Aucun ID trouvé"); //throw error
    }

    // Configurer le chemin vers le WebDriver
    /*
    //get actual dir
    System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    System.out.println("1");
    // Options de Chrome pour éviter les erreurs
    ChromeOptions options = new ChromeOptions();
    System.out.println("2");
    options.addArguments("--headless"); // Exécuter en mode headless, sans interface graphique
    options.addArguments("--disable-gpu"); // Nécessaire pour le mode headless dans certaines versions de Chrome
    System.out.println("3");

    // Créer une instance de WebDriver
    WebDriver driver = new ChromeDriver(options);
    System.out.println("4");

    try {
      // Définir la taille de la fenêtre
      driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

      // Ouvrir la page Airbnb
      String url = logementCreationDto.getUrl();
      driver.get(url);

      // Attendre que la page soit complètement chargée
      Thread.sleep(500);

      // Sélecteur pour trouver le nom de l'hôte
      WebElement hostElement = driver.findElement(By.cssSelector("div[class*='t1pxe1a4']"));
      String hostName = hostElement.getText();

      System.out.println("Nom de l'hôte: " + hostName);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      // Fermer le navigateur
      driver.quit();
    }
    */
    return LogementMapper.entityToDto(
        logementRepository.save(
            LogementMapper.creationDtoToEntity(true, 1, 1, 1, 1, 1, "description", 1f, 1f,
                LocalDateTime.now(), LocalDateTime.now(), 1, 1L, 1L, proprietaire)
        )
    );
  }


}
