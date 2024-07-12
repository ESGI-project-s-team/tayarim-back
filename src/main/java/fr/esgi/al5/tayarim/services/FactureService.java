package fr.esgi.al5.tayarim.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
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
import fr.esgi.al5.tayarim.TayarimApplication;
import fr.esgi.al5.tayarim.dto.facture.FactureCreationDto;
import fr.esgi.al5.tayarim.dto.facture.FactureDto;
import fr.esgi.al5.tayarim.entities.Depense;
import fr.esgi.al5.tayarim.entities.Facture;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Notification;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.DepenseNotFoundError;
import fr.esgi.al5.tayarim.exceptions.FactureBucketUploadError;
import fr.esgi.al5.tayarim.exceptions.FactureDoesNotExistException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.UnauthorizedException;
import fr.esgi.al5.tayarim.mail.EmailService;
import fr.esgi.al5.tayarim.mappers.DepenseMapper;
import fr.esgi.al5.tayarim.mappers.FactureMapper;
import fr.esgi.al5.tayarim.repositories.DepenseRepository;
import fr.esgi.al5.tayarim.repositories.FactureRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.NotificationRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.socket.MyWebSocketHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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

  private final DepenseRepository depenseRepository;

  private final MyWebSocketHandler myWebSocketHandler;
  private final NotificationRepository notificationRepository;

  private final EmailService emailService;

  /**
   * Constructeur pour le service de Facture.
   *
   * @param factureRepository      Le repository des factures.
   * @param proprietaireRepository Le repository des propriétaires.
   * @param logementRepository     Le repository des logements.
   * @param reservationRepository  Le repository des réservations.
   * @param depenseRepository      Le repository des dépenses.
   * @param myWebSocketHandler     Le service de socket.
   * @param notificationRepository Le repository des notifications.
   * @param emailService           Le service des emails
   */
  public FactureService(FactureRepository factureRepository,
      ProprietaireRepository proprietaireRepository, LogementRepository logementRepository,
      ReservationRepository reservationRepository, DepenseRepository depenseRepository,
      MyWebSocketHandler myWebSocketHandler, NotificationRepository notificationRepository,
      EmailService emailService) {
    this.factureRepository = factureRepository;
    this.proprietaireRepository = proprietaireRepository;
    this.logementRepository = logementRepository;
    this.reservationRepository = reservationRepository;
    this.depenseRepository = depenseRepository;
    this.myWebSocketHandler = myWebSocketHandler;
    this.notificationRepository = notificationRepository;
    this.emailService = emailService;
  }

  /**
   * Crée une facture.
   *
   * @param factureCreationDto Le DTO de création de facture.
   * @return Le DTO de la facture créée.
   */
  @Transactional
  public FactureDto create(@NonNull FactureCreationDto factureCreationDto) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(
        factureCreationDto.getIdProprietaire());
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    List<Logement> logements = logementRepository.findAllByProprietaire(optionalProprietaire.get());

    try {
      return FactureMapper.entityToDto(generateFacture(factureCreationDto, logements,
          optionalProprietaire.get()));
    } catch (IOException e) {
      throw new FactureBucketUploadError();
    }

  }

  /**
   * Récupère toutes les factures.
   *
   * @param userId  L'identifiant de l'utilisateur.
   * @param isAdmin Si l'utilisateur est un administrateur.
   * @return La liste des factures.
   */
  public List<FactureDto> getAll(@NonNull Long userId, @NonNull Boolean isAdmin) {
    List<FactureDto> factureDtoList;
    if (isAdmin) {
      factureDtoList = FactureMapper.entityListToDtoList(factureRepository.findAll());
    } else {
      List<Facture> factures = factureRepository.findAllByProprietaireId(userId).stream().filter(
          Facture::getIsSend
      ).toList();
      factureDtoList = FactureMapper.entityListToDtoList(factures);
    }
    //for each facture add nom and prenom of owner by idProprietaire
    for (FactureDto factureDto : factureDtoList) {
      Optional<Proprietaire> proprietaire = proprietaireRepository.findById(
          factureDto.getIdProprietaire());
      if (proprietaire.isPresent()) {
        factureDto.setNomProprietaire(proprietaire.get().getNom());
        factureDto.setPrenomProprietaire(proprietaire.get().getPrenom());
      }
    }

    return factureDtoList;
  }

  /**
   * Récupère une facture par son identifiant.
   *
   * @param numeroFacture L'identifiant de la facture.
   * @param idUser        L'identifiant de l'utilisateur.
   * @param isAdmin       Si l'utilisateur est un administrateur.
   */
  public FactureDto getById(@NonNull String numeroFacture, @NonNull Long idUser,
      @NonNull Boolean isAdmin) {
    if (numeroFacture.length() < 6) {
      numeroFacture = String.format("%06d", Integer.parseInt(numeroFacture));
    }

    Optional<Facture> optionalFacture = factureRepository.findByNumeroFacture(numeroFacture);
    if (optionalFacture.isEmpty()) {
      throw new FactureDoesNotExistException();
    }

    if (!isAdmin && !optionalFacture.get().getProprietaire().getId().equals(idUser)) {
      throw new FactureDoesNotExistException();
    }

    return FactureMapper.entityToDto(optionalFacture.get());
  }

  /**
   * Supprime une facture.
   */
  @Transactional
  public FactureDto delete(@NonNull Long id) {
    Optional<Facture> optionalFacture = factureRepository.findById(id);
    if (optionalFacture.isEmpty()) {
      throw new FactureDoesNotExistException();
    }

    factureRepository.delete(optionalFacture.get());

    return FactureMapper.entityToDto(optionalFacture.get());
  }

  /**
   * Envoie une facture par email et uen notification.
   */
  @Transactional
  public FactureDto sendFacture(@NonNull Long id) {
    Optional<Facture> optionalFacture = factureRepository.findById(id);
    if (optionalFacture.isEmpty()) {
      throw new FactureDoesNotExistException();
    }

    Facture facture = optionalFacture.get();

    myWebSocketHandler.sendNotif(facture.getProprietaire().getId(), LocalDate.now(),
        "notif_facture", "facture");

    notificationRepository.save(new Notification(
        "facture",
        "notif_facture",
        LocalDate.now(),
        facture.getProprietaire(),
        false
    ));

    //sendMail

    facture.setIsSend(true);
    facture = factureRepository.save(facture);
    Proprietaire proprietaire = facture.getProprietaire();

    System.out.println("Facture envoyée par mail");

    Storage storage = TayarimApplication.bucket.getStorage();

    BlobInfo blobInfo = BlobInfo.newBuilder(TayarimApplication.bucket.getName(),
        "Factures/facture_" + facture.getNumeroFacture() + ".pdf").build();

    Blob blob = storage.get(blobInfo.getBlobId()).toBuilder().setContentType("application/pdf")
        .build();

    emailService.sendFactureEmail(proprietaire.getEmail(), proprietaire.getNom(),
        proprietaire.getPrenom(),
        facture.getNumeroFacture(), facture.getMontant(), blob, proprietaire.getLanguage());
    System.out.println("après Facture envoyée par mail");

    return FactureMapper.entityToDto(facture);
  }


  /**
   * Génère un lien pour une facture.
   */
  public String linkFacture(@NonNull Long idFacture, @NonNull Long idUser,
      @NonNull Boolean isAdmin) {
    String numeroFacture = Long.toString(idFacture);
    if (numeroFacture.length() < 6) {
      numeroFacture = String.format("%06d", Integer.parseInt(numeroFacture));
    }
    Optional<Facture> optionalFacture = factureRepository.findByNumeroFacture(numeroFacture);
    if (optionalFacture.isEmpty()) {
      throw new FactureDoesNotExistException();
    }

    if (!isAdmin && !idUser.equals(optionalFacture.get().getProprietaire().getId())) {
      throw new UnauthorizedException();
    }

    Storage storage = TayarimApplication.bucket.getStorage();

    BlobInfo blobInfo = BlobInfo.newBuilder(TayarimApplication.bucket.getName(),
        "Factures/facture_" + numeroFacture + ".pdf").build();
    // Générer l'URL signée
    return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
        Storage.SignUrlOption.httpMethod(HttpMethod.GET),
        Storage.SignUrlOption.withExtHeaders(new HashMap<>())).toString();

  }

  private Facture generateFacture(FactureCreationDto factureCreationDto, List<Logement> logements,
      Proprietaire proprietaire) throws IOException {

    Long idFacture = factureRepository.count() + 1;
    String numeroFacture = Long.toString(idFacture);

    if (numeroFacture.length() < 6) {
      numeroFacture = String.format("%06d", Integer.parseInt(numeroFacture));
      System.out.println(numeroFacture);
    }
    String filePath = numeroFacture + ".pdf";

    Document document = new Document();
    Facture facture = null;
    Float finalAmount = 0f;
    try {
      PdfWriter.getInstance(document, new FileOutputStream(filePath));
      document.open();

      // Ajouter l'image de puis le dossier resources
      String logoPath = "backUtils/white-logo-short-removebg.png";
      Blob blob = TayarimApplication.bucket.get(logoPath);
      Image img = Image.getInstance(blob.getContent());
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
      sellerParagraph.add(new Paragraph("numéro fiscal : 342599453"));
      sellerParagraph.add(new Paragraph("Téléphone : 053 708 50 65"));
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
      buyerParagraph.add(new Paragraph("Adresse : " + proprietaire.getAdresse()));
      buyerParagraph.add(new Paragraph("Téléphone : " + proprietaire.getNumTel()));
      buyerParagraph.add(new Paragraph("Email : " + proprietaire.getEmail()));
      cell2.addElement(buyerParagraph);
      tableClient.addCell(cell2);

      document.add(tableClient);

      Paragraph factureParagraph = new Paragraph(
          "Facture N° " + numeroFacture + " pour le mois de " + Month.of(
                  Math.toIntExact(factureCreationDto.getMonth()))
              .getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRENCH) + " "
              + factureCreationDto.getYear(),
          FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
      factureParagraph.setAlignment(Element.ALIGN_LEFT);
      document.add(factureParagraph);
      LocalDate monthYear = LocalDate.of(Math.toIntExact(factureCreationDto.getYear()),
          Math.toIntExact(factureCreationDto.getMonth()), 1);
      Paragraph dateParagraph = new Paragraph("Date : " + monthYear);
      dateParagraph.setAlignment(Element.ALIGN_LEFT);
      document.add(dateParagraph);

      Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
      cellFont.setColor(0, 0, 0);

      // Get the first day of the month as a LocalDate

      // Ajouter les lignes de dépense
      PdfPTable table = new PdfPTable(3); // 4 colonnes
      table.setWidthPercentage(100);
      float[] tableColumnWidths = {6f, 2f, 2f}; // Ajuster les largeurs des colonnes
      table.setWidths(tableColumnWidths);
      table.setSpacingBefore(10f);
      table.setSpacingAfter(10f);

      Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

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

      table.addCell(descHeaderCell);
      table.addCell(creditDebitHeaderCell);
      table.addCell(totalHeaderCell);

      // Create a YearMonth instance for the given year and month
      YearMonth yearMonth = YearMonth.of(Math.toIntExact(factureCreationDto.getYear()),
          Math.toIntExact(factureCreationDto.getMonth()));

      LocalDate firstDayOfMonth = yearMonth.atDay(1);
      LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

      System.out.println("Logements : " + logements.size());
      boolean secondaryColor = false;
      for (Logement logement : logements) {
        float total = 0f;
        List<Reservation> reservations = reservationRepository
            .findAllByLogementIdAndStatutInAndDateDepartBetween(
                logement.getId(), List.of("done"), firstDayOfMonth, lastDayOfMonth);

        generateLogementCell(table, logement, secondaryColor);
        secondaryColor = !secondaryColor;

        if (logement.getIsLouable()) {

          for (Reservation reservation : reservations) {
            generateReservationCell(table, reservation, secondaryColor);
            total += reservation.getMontant();
            secondaryColor = !secondaryColor;
            generateCommissionCell(table, reservation, proprietaire, secondaryColor);
            total -= (reservation.getMontant() * proprietaire.getCommission()) / 100;
            secondaryColor = !secondaryColor;
          }
        } else {
          generateConciergerieCell(table, logement, secondaryColor);
          total -= logement.getPrixParNuit();
          secondaryColor = !secondaryColor;
        }

        List<Depense> depenses = depenseRepository.findAllByLogementIdAndDateBetween(
            logement.getId(), firstDayOfMonth, lastDayOfMonth);

        for (Depense depense : depenses) {
          generateDepenseCell(table, depense, secondaryColor);
          total -= depense.getPrix();
          secondaryColor = !secondaryColor;
        }

        generateTotalCell(table, total, secondaryColor);
        finalAmount += total;
        secondaryColor = !secondaryColor;
      }
      document.add(table);

      PdfPTable totalTable = new PdfPTable(2);
      totalTable.setWidthPercentage(100);
      float[] totalColumnWidths = {6f, 2f};
      totalTable.setWidths(totalColumnWidths);
      totalTable.setSpacingBefore(10f);
      totalTable.setSpacingAfter(10f);

      generateFinalTotalCell(totalTable, finalAmount);

      document.add(totalTable);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      document.close();

      // get the file just created
      File file = new File(filePath);

      // send to GCS
      String fileName = "Factures/facture_" + numeroFacture + ".pdf";
      TayarimApplication.bucket.create(fileName, Files.readAllBytes(file.toPath()));
      String url = "https://storage.googleapis.com/tayarim-tf-storage/" + fileName;
      file.delete();
      LocalDate monthYear = LocalDate.of(Math.toIntExact(factureCreationDto.getYear()),
          Math.toIntExact(factureCreationDto.getMonth()), 1);
      facture = factureRepository.save(
          FactureMapper.creationDtoToEntity(
              idFacture, proprietaire, numeroFacture, finalAmount, url,
              monthYear
          )
      );
    }
    return facture;
  }

  private void generateLogementCell(PdfPTable table, Logement logement, Boolean secondaryColor) {
    System.out.println("Logement : " + logement.getAdresse());
    Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
    cellFont.setColor(0, 0, 0);
    cellFont.setStyle(Font.BOLD);

    PdfPCell descCell = new PdfPCell(
        new Phrase(
            "Logement : " + logement.getAdresse() + "\n",
            cellFont
        )
    );

    descCell.setPaddingBottom(5);
    descCell.setBorder(Rectangle.NO_BORDER);
    descCell.enableBorderSide(Rectangle.LEFT);
    descCell.enableBorderSide(Rectangle.RIGHT);
    descCell.enableBorderSide(Rectangle.TOP);

    PdfPCell emptyCell = new PdfPCell(new Phrase(""));

    emptyCell.setBorder(Rectangle.NO_BORDER);
    emptyCell.enableBorderSide(Rectangle.TOP);
    emptyCell.enableBorderSide(Rectangle.LEFT);
    emptyCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell emptyCell2 = new PdfPCell(new Phrase(""));

    emptyCell2.setBorder(Rectangle.NO_BORDER);
    emptyCell2.enableBorderSide(Rectangle.TOP);
    emptyCell2.enableBorderSide(Rectangle.LEFT);
    emptyCell2.enableBorderSide(Rectangle.RIGHT);

    if (secondaryColor) {
      BaseColor lightGrey = new BaseColor(220, 220, 220);
      descCell.setBackgroundColor(lightGrey);
      emptyCell.setBackgroundColor(lightGrey);
      emptyCell2.setBackgroundColor(lightGrey);
    } else {
      BaseColor white = new BaseColor(255, 255, 255);
      descCell.setBackgroundColor(white);
      emptyCell.setBackgroundColor(white);
      emptyCell2.setBackgroundColor(white);
    }

    table.addCell(descCell);
    table.addCell(emptyCell);
    table.addCell(emptyCell2);

  }

  private void generateReservationCell(PdfPTable table, Reservation reservation,
      Boolean secondaryColor) {

    Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
    cellFont.setColor(0, 0, 0);

    PdfPCell descCell = new PdfPCell(
        new Phrase(
            "        Réservation du "
                + reservation.getDateArrivee().toString()
                + " au "
                + reservation.getDateDepart().toString()
                + "\n"
                + "        "
                + reservation.getNbPersonnes()
                + " personnes pour "
                + (reservation.getDateDepart().toEpochDay() - reservation.getDateArrivee()
                .toEpochDay())
                + " nuits\n",
            cellFont
        )
    );

    descCell.setPaddingBottom(5);

    descCell.setBorder(Rectangle.NO_BORDER);
    descCell.enableBorderSide(Rectangle.LEFT);
    descCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell creditDebitCell = new PdfPCell(
        new Phrase(
            "\n" + String.format("%.2f", reservation.getMontant()) + " €\n",
            cellFont
        )
    );
    creditDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    creditDebitCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    creditDebitCell.setPaddingBottom(5);
    creditDebitCell.setBorder(Rectangle.NO_BORDER);
    creditDebitCell.enableBorderSide(Rectangle.LEFT);
    creditDebitCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell emptyCell = new PdfPCell(new Phrase(""));
    emptyCell.setBorder(Rectangle.NO_BORDER);
    emptyCell.enableBorderSide(Rectangle.LEFT);
    emptyCell.enableBorderSide(Rectangle.RIGHT);

    if (secondaryColor) {
      BaseColor lightGrey = new BaseColor(220, 220, 220);
      descCell.setBackgroundColor(lightGrey);
      creditDebitCell.setBackgroundColor(lightGrey);
      emptyCell.setBackgroundColor(lightGrey);
    } else {
      BaseColor white = new BaseColor(255, 255, 255);
      descCell.setBackgroundColor(white);
      creditDebitCell.setBackgroundColor(white);
      emptyCell.setBackgroundColor(white);
    }

    table.addCell(descCell);
    table.addCell(creditDebitCell);
    table.addCell(emptyCell);

  }

  private void generateCommissionCell(PdfPTable table, Reservation reservation,
      Proprietaire proprietaire, Boolean secondaryColor) {

    Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
    cellFont.setColor(0, 0, 0);

    PdfPCell descCell = new PdfPCell(
        new Phrase(
            "        commission ("
                + String.format("%.2f", proprietaire.getCommission())
                + "%)",
            cellFont
        )
    );

    descCell.setPaddingBottom(5);
    descCell.setBorder(Rectangle.NO_BORDER);
    descCell.enableBorderSide(Rectangle.LEFT);
    descCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell creditDebitCell = new PdfPCell(
        new Phrase(
            "- " + String.format("%.2f",
                (reservation.getMontant() * proprietaire.getCommission()) / 100) + " €",
            cellFont
        )
    );
    creditDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    creditDebitCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    creditDebitCell.setPaddingBottom(5);
    creditDebitCell.setBorder(Rectangle.NO_BORDER);
    creditDebitCell.enableBorderSide(Rectangle.LEFT);
    creditDebitCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell emptyCell = new PdfPCell(new Phrase(""));
    emptyCell.setBorder(Rectangle.NO_BORDER);
    emptyCell.enableBorderSide(Rectangle.LEFT);
    emptyCell.enableBorderSide(Rectangle.RIGHT);

    if (secondaryColor) {
      BaseColor lightGrey = new BaseColor(220, 220, 220);
      descCell.setBackgroundColor(lightGrey);
      creditDebitCell.setBackgroundColor(lightGrey);
      emptyCell.setBackgroundColor(lightGrey);
    } else {
      BaseColor white = new BaseColor(255, 255, 255);
      descCell.setBackgroundColor(white);
      creditDebitCell.setBackgroundColor(white);
      emptyCell.setBackgroundColor(white);
    }

    table.addCell(descCell);
    table.addCell(creditDebitCell);
    table.addCell(emptyCell);

  }

  private void generateDepenseCell(PdfPTable table, Depense depense, Boolean secondaryColor) {

    Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
    cellFont.setColor(0, 0, 0);

    PdfPCell descCell = new PdfPCell(
        new Phrase(
            "        " + depense.getLibelle(),
            cellFont
        )
    );

    descCell.setPaddingBottom(5);
    descCell.setBorder(Rectangle.NO_BORDER);
    descCell.enableBorderSide(Rectangle.LEFT);
    descCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell creditDebitCell = new PdfPCell(
        new Phrase(
            "- " + String.format("%.2f", depense.getPrix()) + " €",
            cellFont
        )
    );
    creditDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    creditDebitCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    creditDebitCell.setPaddingBottom(5);
    creditDebitCell.setBorder(Rectangle.NO_BORDER);
    creditDebitCell.enableBorderSide(Rectangle.RIGHT);
    creditDebitCell.enableBorderSide(Rectangle.LEFT);

    PdfPCell emptyCell = new PdfPCell(new Phrase(""));
    emptyCell.setBorder(Rectangle.NO_BORDER);
    emptyCell.enableBorderSide(Rectangle.RIGHT);
    emptyCell.enableBorderSide(Rectangle.LEFT);

    if (secondaryColor) {
      BaseColor lightGrey = new BaseColor(220, 220, 220);
      descCell.setBackgroundColor(lightGrey);
      creditDebitCell.setBackgroundColor(lightGrey);
      emptyCell.setBackgroundColor(lightGrey);
    } else {
      BaseColor white = new BaseColor(255, 255, 255);
      descCell.setBackgroundColor(white);
      creditDebitCell.setBackgroundColor(white);
      emptyCell.setBackgroundColor(white);
    }

    table.addCell(descCell);
    table.addCell(creditDebitCell);
    table.addCell(emptyCell);

  }

  private void generateTotalCell(PdfPTable table, Float total, Boolean secondaryColor) {

    Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
    cellFont.setColor(0, 0, 0);

    PdfPCell descCell = new PdfPCell(new Phrase("", cellFont));

    descCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    descCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    descCell.setPaddingBottom(5);
    descCell.setBorder(Rectangle.NO_BORDER);
    descCell.enableBorderSide(Rectangle.LEFT);
    descCell.enableBorderSide(Rectangle.RIGHT);
    descCell.enableBorderSide(Rectangle.BOTTOM);

    PdfPCell totalCell = new PdfPCell(
        new Phrase(String.format("%.2f", total) + " €", cellFont));
    totalCell.setBorder(Rectangle.NO_BORDER);
    totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    totalCell.setPaddingBottom(5);
    totalCell.enableBorderSide(Rectangle.LEFT);
    totalCell.enableBorderSide(Rectangle.RIGHT);
    totalCell.enableBorderSide(Rectangle.BOTTOM);

    PdfPCell emptyCell = new PdfPCell(new Phrase(""));
    emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    emptyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    emptyCell.setPaddingBottom(5);
    emptyCell.setBorder(Rectangle.NO_BORDER);
    emptyCell.enableBorderSide(Rectangle.LEFT);
    emptyCell.enableBorderSide(Rectangle.RIGHT);
    emptyCell.enableBorderSide(Rectangle.BOTTOM);

    if (secondaryColor) {
      BaseColor lightGrey = new BaseColor(220, 220, 220);
      descCell.setBackgroundColor(lightGrey);
      totalCell.setBackgroundColor(lightGrey);
      emptyCell.setBackgroundColor(lightGrey);
    } else {
      BaseColor white = new BaseColor(255, 255, 255);
      descCell.setBackgroundColor(white);
      totalCell.setBackgroundColor(white);
      emptyCell.setBackgroundColor(white);
    }

    table.addCell(descCell);
    table.addCell(emptyCell);
    table.addCell(totalCell);

  }

  private void generateConciergerieCell(PdfPTable table, Logement logement,
      Boolean secondaryColor) {

    Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
    cellFont.setColor(0, 0, 0);

    PdfPCell descCell = new PdfPCell(
        new Phrase(
            "        Conciergerie",
            cellFont
        )
    );

    descCell.setPaddingBottom(5);
    descCell.setBorder(Rectangle.NO_BORDER);
    descCell.enableBorderSide(Rectangle.LEFT);
    descCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell creditDebitCell = new PdfPCell(
        new Phrase(
            "- " + logement.getPrixParNuit() + " €",
            cellFont
        )
    );
    creditDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    creditDebitCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    creditDebitCell.setPaddingBottom(5);
    creditDebitCell.setBorder(Rectangle.NO_BORDER);
    creditDebitCell.enableBorderSide(Rectangle.LEFT);
    creditDebitCell.enableBorderSide(Rectangle.RIGHT);

    PdfPCell emptyCell = new PdfPCell(new Phrase(""));
    emptyCell.setBorder(Rectangle.NO_BORDER);
    emptyCell.enableBorderSide(Rectangle.LEFT);
    emptyCell.enableBorderSide(Rectangle.RIGHT);

    if (secondaryColor) {
      BaseColor lightGrey = new BaseColor(220, 220, 220);
      descCell.setBackgroundColor(lightGrey);
      creditDebitCell.setBackgroundColor(lightGrey);
      emptyCell.setBackgroundColor(lightGrey);
    } else {
      BaseColor white = new BaseColor(255, 255, 255);
      descCell.setBackgroundColor(white);
      creditDebitCell.setBackgroundColor(white);
      emptyCell.setBackgroundColor(white);
    }

    table.addCell(descCell);
    table.addCell(creditDebitCell);
    table.addCell(emptyCell);

  }

  private void generateFinalTotalCell(PdfPTable table, Float finalTotal) {

    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

    PdfPCell descCell = new PdfPCell(new Phrase("Total", headerFont));
    descCell.setBackgroundColor(new BaseColor(173, 216, 230));
    descCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    descCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    descCell.setPaddingBottom(5);

    PdfPCell totalCell = new PdfPCell(
        new Phrase(String.format("%.2f", finalTotal) + " €", headerFont));

    descCell.setBackgroundColor(new BaseColor(173, 216, 230));

    totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    totalCell.setPaddingBottom(5);

    table.addCell(descCell);
    table.addCell(totalCell);

  }

}
