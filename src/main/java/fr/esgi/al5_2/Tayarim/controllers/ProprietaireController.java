package fr.esgi.al5_2.Tayarim.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.esgi.al5_2.Tayarim.dto.auth.AuthDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireLoginDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireLoginResponseDTO;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import fr.esgi.al5_2.Tayarim.services.ProprietaireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/proprietaires")
public class ProprietaireController {

    private final ProprietaireService proprietaireService;

    public ProprietaireController(ProprietaireService proprietaireService) {
        this.proprietaireService = proprietaireService;
    }

    @PostMapping("")
    public ResponseEntity<ProprietaireDTO> creerProprietaire(@Valid @RequestBody ProprietaireCreationDTO proprietaireCreationDTO) {
        return new ResponseEntity<>(
            proprietaireService.creerProprietaire(proprietaireCreationDTO),
            HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<ProprietaireDTO>> getProprietaire(@RequestParam(name = "logement", defaultValue = "false") Boolean isLogement){
        return new ResponseEntity<>(
                proprietaireService.getProprietaire(isLogement),
                HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ProprietaireLoginResponseDTO> loginProprietaire(@Valid @RequestBody ProprietaireLoginDTO proprietaireLoginDTO){
        return new ResponseEntity<>(
                proprietaireService.loginProprietaire(proprietaireLoginDTO.getEmail(),proprietaireLoginDTO.getMotDePasse()),
                HttpStatus.OK
        );
    }

    @PostMapping("/auth")
    public ResponseEntity<ProprietaireLoginResponseDTO> authProprietaire(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody AuthDTO authDTO){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TokenExpireOrInvalidException();
        }

        String jwtToken = authHeader.substring(7);

        return new ResponseEntity<>(
                proprietaireService.authProprietaire(jwtToken, authDTO.getId()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProprietaireDTO> getProprietaire(@PathVariable Long id, @RequestParam(name = "logement", defaultValue = "false") Boolean isLogement){
        return new ResponseEntity<>(
                proprietaireService.getProprietaireById(id, isLogement),
                HttpStatus.OK
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        ArrayList<String> errors = new ArrayList<>();
        Map<String, List<String>> errorMapping = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        errorMapping.put("errors", errors);

        return errorMapping;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ProprietaireEmailAlreadyExistException.class})
    public Map<String, List<String>> proprietaireEmailAlreadyExistException(ProprietaireEmailAlreadyExistException ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ProprietaireNumTelAlreadyExistException.class})
    public Map<String, List<String>> proprietaireNumTelAlreadyExistException(ProprietaireNumTelAlreadyExistException ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ProprietaireNotFoundException.class})
    public Map<String, List<String>> proprietaireNotFoundException(ProprietaireNotFoundException ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({PasswordHashNotPossibleException.class})
    public Map<String, List<String>> passwordHashNotPossibleException(PasswordHashNotPossibleException ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({TokenExpireOrInvalidException.class})
    public Map<String, List<String>> tokenExpireOrInvalidException(TokenExpireOrInvalidException ex){
        return mapException(ex);
    }

    private Map<String, List<String>> mapException(RuntimeException exception){
        ArrayList<String> errors = new ArrayList<>();
        Map<String, List<String>> errorMapping = new HashMap<>();
        errors.add(exception.getMessage());

        errorMapping.put("errors", errors);

        return errorMapping;
    }
}
