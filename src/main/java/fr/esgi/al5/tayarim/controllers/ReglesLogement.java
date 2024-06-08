package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.DeleteMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.UpdateMethodInterface;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des règles de logement.
 */
@RestController
@RequestMapping("/reglesLogement")
public class ReglesLogement implements
    CreateMethodInterface<LogementDto, LogementCreationDto>,
    GetAllMethodInterface<LogementDto>,
    GetByIdMethodInterface<LogementDto>,
    UpdateMethodInterface<LogementDto, LogementUpdateDto>,
    DeleteMethodInterface<LogementDto> {

  @Override
  public ResponseEntity<LogementDto> create(String authHeader, LogementCreationDto object) {
    return null;
  }

  @Override
  public ResponseEntity<List<LogementDto>> getAll(String authHeader) {
    return null;
  }

  @Override
  public ResponseEntity<LogementDto> getById(String authHeader, Long id) {
    return null;
  }

  @Override
  public ResponseEntity<LogementDto> update(String authHeader, Long id, LogementUpdateDto object) {
    return null;
  }

  @Override
  public ResponseEntity<LogementDto> delete(String authHeader, Long id) {
    return null;
  }
}
