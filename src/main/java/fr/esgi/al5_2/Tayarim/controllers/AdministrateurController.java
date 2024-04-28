package fr.esgi.al5_2.Tayarim.controllers;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.*;
import fr.esgi.al5_2.Tayarim.entities.Administrateur;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import fr.esgi.al5_2.Tayarim.services.AdministrateurService;
import fr.esgi.al5_2.Tayarim.services.AuthService;
import fr.esgi.al5_2.Tayarim.services.ProprietaireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdministrateurController {

    private final AdministrateurService administrateurService;
    private final AuthService authService;
    public AdministrateurController(AdministrateurService administrateurService, AuthService authService) {
        this.administrateurService = administrateurService;
        this.authService = authService;
    }

    /*
    @PostMapping("")
    public ResponseEntity<AdministrateurDTO> creerAdministrateur(@Valid @RequestBody AdministrateurCreationDTO administrateurCreationDTO) {
        return new ResponseEntity<>(
            administrateurService.creerAdministrateur(administrateurCreationDTO),
            HttpStatus.CREATED);
    }*/

    @GetMapping("")
    public ResponseEntity<List<AdministrateurDTO>> getAdministrateur(@RequestHeader("Authorization") String authHeader){
        authService.verifyToken(getTokenFromHeader(authHeader), true);

        return new ResponseEntity<>(
                administrateurService.getAdministrateur(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> getAdministrateur(@RequestHeader("Authorization") String authHeader, @PathVariable Long id){

        authService.verifyToken(getTokenFromHeader(authHeader), true);

        return new ResponseEntity<>(
                administrateurService.getAdministrateurById(id),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> updateAdministrateur(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @RequestBody AdministrateurUpdateDTO administrateurUpdateDTO){
        Long idToken = authService.verifyToken(getTokenFromHeader(authHeader), true);

        if(!idToken.equals(id)){
            throw new UnauthorizedException();
        }

        return new ResponseEntity<>(
                administrateurService.updateAdministrateur(id, administrateurUpdateDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> deleteAdministrateur(@RequestHeader("Authorization") String authHeader, @PathVariable Long id){
        Long idToken = authService.verifyToken(getTokenFromHeader(authHeader), true);
        if(!idToken.equals(id)){
            throw new UnauthorizedException();
        }

        return new ResponseEntity<>(
                administrateurService.deleteAdministrateur(id),
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
    @ExceptionHandler(AdministrateurInvalidUpdateBody.class)
    public Map<String, List<String>> administrateurInvalidUpdateBody(AdministrateurInvalidUpdateBody ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({AdministrateurEmailAlreadyExistException.class})
    public Map<String, List<String>> administrateurEmailAlreadyExistException(AdministrateurEmailAlreadyExistException ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({AdministrateurNumTelAlreadyExistException.class})
    public Map<String, List<String>> administrateurNumTelAlreadyExistException(AdministrateurNumTelAlreadyExistException ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({AdministrateurNotFoundException.class})
    public Map<String, List<String>> administrateurNotFoundException(AdministrateurNotFoundException ex){
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
