package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant un Avis dans le système. La gestion des Avis est
 * effectuée en liaison avec les logement à travers une relation ManyToOne.
 */
@Data
@Entity(name = "AVIS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Avis {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "TEXTE", nullable = false)
  private String texte;

  @Column(name = "PSEUDO", nullable = false)
  private String pseudo;

  @Column(name = "NOTE", nullable = false)
  private Float note;

  @Column(name = "DATE", nullable = false)
  private LocalDateTime date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

}
