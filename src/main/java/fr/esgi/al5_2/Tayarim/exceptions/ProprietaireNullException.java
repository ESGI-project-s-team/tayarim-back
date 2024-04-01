package fr.esgi.al5_2.Tayarim.exceptions;

public class ProprietaireNullException extends RuntimeException {
    public ProprietaireNullException() {
        super("Proprietaire ne peut être nul");
    }
}