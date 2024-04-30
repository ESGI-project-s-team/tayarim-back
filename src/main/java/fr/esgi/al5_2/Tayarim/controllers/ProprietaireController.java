package fr.esgi.al5_2.Tayarim.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireUpdateDTO;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import fr.esgi.al5_2.Tayarim.services.AuthService;
import fr.esgi.al5_2.Tayarim.services.ProprietaireService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/proprietaires")
public class ProprietaireController {

    private final ProprietaireService proprietaireService;
    private final AuthService authService;
    public ProprietaireController(ProprietaireService proprietaireService, AuthService authService) {
        this.proprietaireService = proprietaireService;
        this.authService = authService;
    }

    @PostMapping("")
    public ResponseEntity<ProprietaireDTO> creerProprietaire(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ProprietaireCreationDTO proprietaireCreationDTO) {
        authService.verifyToken(getTokenFromHeader(authHeader), true);
        return new ResponseEntity<>(
                proprietaireService.creerProprietaire(proprietaireCreationDTO),
                HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<ProprietaireDTO>> getProprietaire(@RequestHeader("Authorization") String authHeader, @RequestParam(name = "logement", defaultValue = "false") Boolean isLogement){
        authService.verifyToken(getTokenFromHeader(authHeader), true);

        return new ResponseEntity<>(
                proprietaireService.getProprietaire(isLogement),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProprietaireDTO> getProprietaire(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @RequestParam(name = "logement", defaultValue = "false") Boolean isLogement){

        authService.verifyToken(getTokenFromHeader(authHeader), false);

        return new ResponseEntity<>(
                proprietaireService.getProprietaireById(id, isLogement),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProprietaireDTO> updateProprietaire(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @RequestBody ProprietaireUpdateDTO proprietaireUpdateDTO){
        authService.verifyToken(getTokenFromHeader(authHeader), true);

        return new ResponseEntity<>(
                proprietaireService.updateProprietaire(id, proprietaireUpdateDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProprietaireDTO> deleteProprietaire(@RequestHeader("Authorization") String authHeader, @PathVariable Long id){
        Long idToken = authService.verifyToken(getTokenFromHeader(authHeader), true).getKey();
        if(!idToken.equals(id)){
            throw new UnauthorizedException();
        }

        return new ResponseEntity<>(
                proprietaireService.deleteProprietaire(id),
                HttpStatus.OK
        );
    }

    private String getTokenFromHeader(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TokenExpireOrInvalidException();
        }

        return authHeader.substring(7);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProprietaireInvalidUpdateBody.class)
    public Map<String, List<String>> proprietaireInvalidUpdateBody(ProprietaireInvalidUpdateBody ex){
        return mapException(ex);
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class})
    public Map<String, List<String>> unauthorizedException(UnauthorizedException ex){
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