package fr.esgi.al5.tayarim.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.services.AuthService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Service de socket pour les notifications.
 */
@Service
public class MyWebSocketHandler extends TextWebSocketHandler {

  private HashMap<Long, WebSocketSession> sessions;

  private final AuthService authService;

  private final ObjectMapper objectMapper;

  /**
   * Constructeur pour le service de socket.
   *
   * @param authService  service d'authentification
   * @param objectMapper mapper json
   */
  public MyWebSocketHandler(AuthService authService, ObjectMapper objectMapper) {
    this.authService = authService;
    this.objectMapper = objectMapper;
    this.sessions = new HashMap<>();
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    System.out.println("Connection established: " + session.getId());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    System.out.println("Message received: " + message.getPayload());
    String messagePayload = message.getPayload();

    if (message.getPayload().startsWith("Connect : Bearer ")) {
      String token = message.getPayload().substring(17);
      try {
        UserTokenInfo userTokenInfo = authService.verifyToken(token, false);
        sessions.put(userTokenInfo.getId(), session);
      } catch (TokenExpireOrInvalidException e) {
        session.sendMessage(new TextMessage("Rejected : Invalid token"));
      }
    }

  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    Long idToRemove = -1L;
    for (Entry<Long, WebSocketSession> e : sessions.entrySet()) {
      if (e.getValue().getId().equals(session.getId())) {
        idToRemove = e.getKey();
        break;
      }
    }
    sessions.remove(idToRemove);
    System.out.println("Connection closed: " + session.getId());
  }

  /**
   * Envoie une notification à un utilisateur.
   *
   * @param idUser  id de l'utilisateur
   * @param temp    jour d'envoie de la notif
   * @param message message de la notif
   * @param type    type de la notif
   */
  public void sendNotif(@NonNull Long idUser, @NonNull LocalDate temp,
      @NonNull String message, @NonNull String type) {
    WebSocketSession session = sessions.get(idUser);

    if (session != null) {
      try {
        SocketNotificationDto socketNotificationDto = new SocketNotificationDto(
            temp.toString().substring(0, 10),
            message,
            type
        );
        String jsonMessage = objectMapper.writeValueAsString(socketNotificationDto);
        session.sendMessage(new TextMessage(jsonMessage));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}