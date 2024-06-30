package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class FactureBucketUploadError extends RuntimeException {

  public FactureBucketUploadError() {
    super("error_invoice_upload");
  }
}