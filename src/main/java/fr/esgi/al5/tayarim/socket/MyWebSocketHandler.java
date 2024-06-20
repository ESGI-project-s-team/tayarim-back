package fr.esgi.al5.tayarim.socket;

import fr.esgi.al5.tayarim.services.AuthService;
import java.util.Map;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {
  private Map<Long, String> sessions;

  public MyWebSocketHandler() {
    this.sessions = Map.of();
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    System.out.println("Connection established: " + session.getId());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    System.out.println("Message received: " + message.getPayload());
    String messagePayload = message.getPayload();

    if(message.getPayload().startsWith("Connect : Bearer ")) {
      String token = message.getPayload().substring(16);
      System.out.println("Token received: " + token);

      session.sendMessage(new TextMessage("Hello from server"));
    } else {
      session.sendMessage(new TextMessage("Unknown message received: " + messagePayload));
    }

  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    System.out.println("Connection closed: " + session.getId());
  }
}