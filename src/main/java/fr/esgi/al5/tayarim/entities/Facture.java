package fr.esgi.al5.tayarim.entities;

import com.google.cloud.storage.Blob;
import fr.esgi.al5.tayarim.TayarimApplication;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant une facture.
 */
@Data
@Entity(name = "FACTURE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facture {

  @Id
  /* Pas de generatedValue car on lis le prochaine id
     (et donc incrémente automatiquement comme si on avait fait un insert),
     se qui créerait un décalage
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "facture_seq")
   */
  @SequenceGenerator(name = "facture_seq", sequenceName = "facture_id_seq", allocationSize = 1)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NUMEROFACTURE", nullable = false)
  private String numeroFacture;

  @Column(name = "DATE", nullable = false)
  private LocalDate dateFacture;

  @Column(name = "MONTANT", nullable = false)
  private Float montant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDPROPRIETAIRE", nullable = false)
  private Proprietaire proprietaire;

  @Column(name = "URL", nullable = false)
  private String url;

  @Column(name = "ISSEND", nullable = false)
  private Boolean isSend;


  /**
   * Constructeur par défaut de la classe Facture.
   */
  @Builder
  public Facture(Long id, String numeroFacture, LocalDate dateFacture, Float montant,
      Proprietaire proprietaire, String url, Boolean isSend) {
    this.id = id;
    this.numeroFacture = numeroFacture;
    this.dateFacture = dateFacture;
    this.montant = montant;
    this.proprietaire = proprietaire;
    this.url = url;
    this.isSend = isSend;
  }

  /**
   * Méthode appelée avant la suppression d'une facture.
   */
  @PreRemove()
  public void beforeDelete() {
    String filePath = this.url.substring(this.url.indexOf("Factures/"));

    Blob blob = TayarimApplication.bucket.get(filePath);

    if (blob != null) {
      blob.delete();
    }

  }

}
