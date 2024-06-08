package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception levée lorsqu'une méthode n'est pas supportée pour un chemin donné.
 */
public class UnsupportedMethodPathException extends RuntimeException {

  public UnsupportedMethodPathException() {
    super("error_unsupported_method_path");
  }

}
