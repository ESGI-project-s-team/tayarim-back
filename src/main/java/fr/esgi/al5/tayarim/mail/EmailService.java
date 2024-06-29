package fr.esgi.al5.tayarim.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service pour l'envoi d'email.
 */
@Service
public class EmailService {
  //private final JavaMailSender mailSender;

  /**
   * Constructeur pour le service d'envoi d'email.
   */
  public EmailService(/*JavaMailSender mailSender*/) {
    //this.mailSender = mailSender;
  }

  /**
   * Envoie un email.
   *
   * @param to      L'adresse email du destinataire.
   * @param subject Le sujet de l'email.
   * @param body    Le contenu de l'email.
   */
  public void sendEmail(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);

    //mailSender.send(message);
  }
}