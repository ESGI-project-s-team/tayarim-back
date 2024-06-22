package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un type delogement dans le système. Chaque type de logement peut etre associé
 * à plusieur logement, représenté par l'entité Logement. La gestion des type de logement est
 * effectuée en liaison avec les logement à travers une relation ManyToOne.
 */
@Data
@Entity(name = "NOTIFICATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TYPE", nullable = false)
  private String type;

  @Column(name = "MESSAGE", nullable = false)
  private String message;

  @Column(name = "DATE", nullable = false)
  private LocalDate date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDUSER", nullable = false)
  private Utilisateur utilisateur;

  @Column(name = "ISREAD", nullable = false)
  private Boolean isRead;

  /**
   * Constructeur pour l'entité TypeLogement.
   *
   * @param type Type de logement
   * @param message Message de la notification
   * @param date Date de la notification
   * @param utilisateur Utilisateur concerné par la notification
   * @param isRead Indicateur de lecture de la notification
   */
  @Builder
  public Notification(String type, String message, LocalDate date, Utilisateur utilisateur, Boolean isRead) {
    this.type = type;
    this.message = message;
    this.date = date;
    this.utilisateur = utilisateur;
    this.isRead = isRead;
  }

}
