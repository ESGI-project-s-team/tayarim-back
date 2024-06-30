package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.dto.statistique.StatistiqueDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.StatistiqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistiques")
public class StatistiqueController implements ControllerUtils,
        GetByIdMethodInterface<StatistiqueDto> {
    private final StatistiqueService statistiqueService;
    private final AuthService authService;

    public StatistiqueController(StatistiqueService statistiqueService, AuthService authService) {
        this.statistiqueService = statistiqueService;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<StatistiqueDto> getById(String authHeader, Long id) {
        UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);

        return new ResponseEntity<>(
                statistiqueService.getStatistiqueParAnnee(id, userTokenInfo.getIsAdmin(), userTokenInfo.getId()),
                HttpStatus.OK
        );
    }
}
