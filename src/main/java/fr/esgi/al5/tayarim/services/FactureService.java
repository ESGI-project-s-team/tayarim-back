package fr.esgi.al5.tayarim.services;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import fr.esgi.al5.tayarim.dto.facture.FactureCreationDto;
import fr.esgi.al5.tayarim.dto.facture.FactureDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.mappers.FactureMapper;
import fr.esgi.al5.tayarim.repositories.FactureRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les factures.
 */
@Service
@Transactional(readOnly = true)
public class FactureService {

  private final FactureRepository factureRepository;
  private final ProprietaireRepository proprietaireRepository;
  private final LogementRepository logementRepository;
  private final ReservationRepository reservationRepository;

  /**
   * Constructeur pour le service de Facture.
   *
   * @param factureRepository      Le repository des factures.
   * @param proprietaireRepository Le repository des propriétaires.
   * @param logementRepository     Le repository des logements.
   * @param reservationRepository  Le repository des réservations.
   */
  public FactureService(FactureRepository factureRepository,
      ProprietaireRepository proprietaireRepository, LogementRepository logementRepository,
      ReservationRepository reservationRepository) {
    this.factureRepository = factureRepository;
    this.proprietaireRepository = proprietaireRepository;
    this.logementRepository = logementRepository;
    this.reservationRepository = reservationRepository;
  }

  /**
   * Crée une facture.
   *
   * @param factureCreationDto Le DTO de création de facture.
   * @return Le DTO de la facture créée.
   */
  public FactureDto create(@NonNull FactureCreationDto factureCreationDto) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(
        factureCreationDto.getIdProprietaire());
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    List<Logement> logements = logementRepository.findAllByProprietaire(optionalProprietaire.get());

    generateFacture(factureCreationDto.getMonth(), factureCreationDto.getYear(), logements, optionalProprietaire.get());

    return null;

  }

  /**
   * Récupère toutes les factures.
   *
   * @param userId  L'identifiant de l'utilisateur.
   * @param isAdmin Si l'utilisateur est un administrateur.
   * @return La liste des factures.
   */
  public List<FactureDto> getAll(@NonNull Long userId, @NonNull Boolean isAdmin) {
    if (isAdmin) {
      return FactureMapper.entityListToDtoList(factureRepository.findAll());
    }

    //return FactureMapper.entityListToDtoList(factureRepository.findByProprietaireId(userId));
    return null;


  }

  private void generateFacture(Long month, Long year, List<Logement> logements, Proprietaire proprietaire) {
    Document document = new Document();
    try {
      PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("facture.pdf"));

      document.open();

      // Ajouter l'image de puis le dossier resources
      String logoPath = "src/main/resources/white-logo-short-removebg.png";
      Image img = Image.getInstance(logoPath);
      img.scaleToFit(100, 100);
      document.add(img);

      PdfPTable tableTayarim = new PdfPTable(2); // 3 colonnes pour plus de flexibilité
      tableTayarim.setWidthPercentage(100);
      float[] tayarimColumnWidths = {2f, 4f}; // Ajuster les largeurs des colonnes
      tableTayarim.setWidths(tayarimColumnWidths);
      tableTayarim.setSpacingBefore(20f);
      tableTayarim.setSpacingAfter(20f);

      // Ajouter les informations de la société
      PdfPCell cell1 = new PdfPCell();
      cell1.setPaddingLeft(10);
      cell1.setPaddingBottom(10);

      cell1.setBorder(Rectangle.BOX);
      Paragraph sellerParagraph = new Paragraph();
      sellerParagraph.add(
          new Paragraph("Société", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
      sellerParagraph.add(new Paragraph("Nom : Tayarim"));
      sellerParagraph.add(new Paragraph("Adresse : "));
      sellerParagraph.add(new Paragraph("Téléphone : "));
      cell1.addElement(sellerParagraph);
      tableTayarim.addCell(cell1);

      // Ajouter une cellule vide pour le centre
      PdfPCell emptyCell = new PdfPCell();
      emptyCell.setBorder(Rectangle.NO_BORDER);
      tableTayarim.addCell(emptyCell);

      document.add(tableTayarim);

      PdfPTable tableClient = new PdfPTable(2); // 3 colonnes pour plus de flexibilité
      tableClient.setWidthPercentage(100);
      float[] clientColumnWidths = {4f, 2f}; // Ajuster les largeurs des colonnes
      tableClient.setWidths(clientColumnWidths);
      tableClient.setSpacingBefore(20f);
      tableClient.setSpacingAfter(20f);

      tableClient.addCell(emptyCell);

      // Ajouter les informations du client
      PdfPCell cell2 = new PdfPCell();
      cell2.setBorder(Rectangle.BOX);
      cell2.setPaddingLeft(10);
      cell2.setPaddingBottom(10);
      Paragraph buyerParagraph = new Paragraph();
      buyerParagraph.add(
          new Paragraph("Client", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
      buyerParagraph.add(new Paragraph("Nom : " + proprietaire.getNom()));
      buyerParagraph.add(new Paragraph("Prénom : " + proprietaire.getPrenom()));
      buyerParagraph.add(new Paragraph("Adresse : "));
      buyerParagraph.add(new Paragraph("Téléphone : " + proprietaire.getNumTel()));
      buyerParagraph.add(new Paragraph("Email : " + proprietaire.getEmail()));
      cell2.addElement(buyerParagraph);
      tableClient.addCell(cell2);

      document.add(tableClient);

      Paragraph factureParagraph = new Paragraph("Facture N° 00001 pour le mois de " + Month.of(
          Math.toIntExact(month)).getDisplayName(TextStyle.FULL, Locale.FRANCE) + " " + year,
          FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
      factureParagraph.setAlignment(Element.ALIGN_LEFT);
      document.add(factureParagraph);

      Paragraph dateParagraph = new Paragraph("Date : " + LocalDate.now());
      dateParagraph.setAlignment(Element.ALIGN_LEFT);
      document.add(dateParagraph);

      // Ajouter les lignes de dépense
      PdfPTable depenseTable = new PdfPTable(3); // 4 colonnes
      depenseTable.setWidthPercentage(100);
      float[] depenseColumnWidths = {6f, 2f, 2f}; // Ajuster les largeurs des colonnes
      depenseTable.setWidths(depenseColumnWidths);
      depenseTable.setSpacingBefore(10f);
      depenseTable.setSpacingAfter(10f);

      Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
      Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
      cellFont.setColor(0, 0, 0);

      PdfPCell descHeaderCell = new PdfPCell(
          new Phrase("Désignation des produits ou prestations", headerFont));
      descHeaderCell.setBackgroundColor(new BaseColor(173, 216, 230));
      descHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      descHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      descHeaderCell.setPaddingBottom(5);

      PdfPCell creditDebitHeaderCell = new PdfPCell(new Phrase("Credit/Debit", headerFont));
      creditDebitHeaderCell.setBackgroundColor(new BaseColor(173, 216, 230));
      creditDebitHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      creditDebitHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      creditDebitHeaderCell.setPaddingBottom(5);

      PdfPCell totalHeaderCell = new PdfPCell(new Phrase("Total", headerFont));
      totalHeaderCell.setBackgroundColor(new BaseColor(173, 216, 230));
      totalHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      totalHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      totalHeaderCell.setPaddingBottom(5);

      depenseTable.addCell(descHeaderCell);
      depenseTable.addCell(creditDebitHeaderCell);
      depenseTable.addCell(totalHeaderCell);

      Float finalAmount = 0f;

      boolean secondaryColor = false;
      BaseColor lightGrey = new BaseColor(220, 220, 220);
      for (Logement logement : logements) {

        String colDesc = "Logement : " + logement.getAdresse();
        String creditDebit = "";
        String total = "";

        List<Reservation> reservations = reservationRepository.findAllByLogementIdAndStatutInAndDateDepartStartsWith(
            logement.getId(), List.of("done"), year + "-" + (month < 10 ? "0" + month : month) + "-%");

        String resaDesc = "";
        String resaCreditDebit = "";
        String resaTotal = "";

        Float totalMontant = 0f;

        if (logement.getIsLouable()) {

          if (!reservations.isEmpty()) {
            resaTotal = resaTotal.concat("\n\n");
          }

          for (Reservation reservation : reservations) {
            resaDesc = resaDesc.concat("\n\n");

            resaCreditDebit = resaCreditDebit.concat("\n\n\n");

            resaTotal = resaTotal.concat("\n\n\n\n");

            resaDesc = resaDesc.concat(
                "        Réservation du " + reservation.getDateArrivee().toString() + " au "
                    + reservation.getDateDepart().toString() + "\n"
                    + "                " + reservation.getNbPersonnes() + " personnes pour "
                    + ((reservation.getDateDepart().toEpochDay() - reservation.getDateArrivee()
                    .toEpochDay()) - 1) + " nuits\n"
                    + "                comission (" + String.format("%.2f",
                    proprietaire.getCommission()) + "%)"
            );

            float commission = (reservation.getMontant() * proprietaire.getCommission()) / 100;
            resaCreditDebit = resaCreditDebit.concat(
                String.format("%.2f", reservation.getMontant()) + " €\n"
                    + "- " + commission + " €");
            totalMontant += reservation.getMontant();
            totalMontant -= commission;

          }
        } else {
          resaDesc = resaDesc.concat("\n\n");
          resaDesc = resaDesc.concat("        Conciergerie");

          resaCreditDebit = resaCreditDebit.concat("\n\n");
          resaCreditDebit = resaCreditDebit.concat(
              "- " + String.format("%.2f", logement.getPrixParNuit()) + " €\n");

          totalMontant -= logement.getPrixParNuit();
          resaTotal = resaTotal.concat("\n\n\n");
        }

        resaTotal = resaTotal.concat(String.format(String.format("%.2f", totalMontant) + " €"));

        PdfPCell descCell = new PdfPCell(new Phrase(colDesc.concat(resaDesc), cellFont));
        PdfPCell creditDebitCell = new PdfPCell(
            new Phrase(creditDebit.concat(resaCreditDebit), cellFont));
        PdfPCell totalCell = new PdfPCell(new Phrase(total.concat(resaTotal), cellFont));
        if (secondaryColor) {
          descCell.setBackgroundColor(lightGrey);
          creditDebitCell.setBackgroundColor(lightGrey);
          totalCell.setBackgroundColor(lightGrey);
        }
        descCell.setPaddingBottom(5);
        creditDebitCell.setPaddingBottom(5);
        totalCell.setPaddingBottom(5);

        descCell.setPaddingLeft(5);
        creditDebitCell.setPaddingRight(5);
        creditDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setPaddingRight(5);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        depenseTable.addCell(descCell);
        depenseTable.addCell(creditDebitCell);
        depenseTable.addCell(totalCell);

        secondaryColor = !secondaryColor;

        finalAmount += totalMontant;
      }
      document.add(depenseTable);

      PdfPTable totalTable = new PdfPTable(2);
      totalTable.setWidthPercentage(100);
      float[] totalColumnWidths = {6f, 2f};
      totalTable.setWidths(totalColumnWidths);
      totalTable.setSpacingBefore(10f);
      totalTable.setSpacingAfter(10f);

      PdfPCell totalDescCell = new PdfPCell(new Phrase("Total", headerFont));
      totalDescCell.setBackgroundColor(new BaseColor(173, 216, 230));
      totalDescCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      totalDescCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      totalDescCell.setPaddingBottom(5);

      PdfPCell totalCell = new PdfPCell(
          new Phrase(String.format("%.2f", finalAmount) + " €", headerFont));
      if (secondaryColor) {
        totalCell.setBackgroundColor(lightGrey);
      }
      totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      totalCell.setPaddingBottom(5);

      totalTable.addCell(totalDescCell);
      totalTable.addCell(totalCell);

      document.add(totalTable);

      document.close();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      document.close();
    }
  }

}
