package fr.esgi.al5.tayarim.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UtilisateurNotFoundException;
import fr.esgi.al5.tayarim.mappers.LogementMapper;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.core.env.Environment;
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

  private final Environment env;


  /**
   * Constructeur pour le service de logement.
   *
   * @param logementRepository Le repository des logements.
   */
  public LogementService(LogementRepository logementRepository,
      ProprietaireRepository proprietaireRepository, Environment env) {
    this.logementRepository = logementRepository;
    this.proprietaireRepository = proprietaireRepository;
    this.env = env;
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

    return LogementMapper.entityToDto(
        logementRepository.save(
            LogementMapper.creationDtoToEntity(
                logementCreationDto,
                1L,
                1L,
                proprietaire)
        )
    );
  }

  /**
   * Tente de récupérer tous les logements.
   *
   * @return {@link LogementDto}
   */
  @Transactional
  public List<LogementDto> getAllLogement() {
    return LogementMapper.entityListToDtoList(
        logementRepository.findAll()
    );
  }

  /**
   * Tente de récupérer un logements par son id.
   *
   * @return {@link LogementDto}
   */
  @Transactional
  public LogementDto getLogementById(@NonNull Long id) {
    Optional<Logement> optionalLogement = logementRepository.findById(id);

    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    return LogementMapper.entityToDto(optionalLogement.get());
  }


}
