/**
 * La classe Reservation représente une réservation effectuée par un voyageur pour un voyage donné.
 */
public class Reservation {
    
    /** L'identifiant unique de la réservation. */
    private int id;
    
    /** L'identifiant du voyageur ayant effectué la réservation. */
    private int idVoyageur;
    
    /** L'identifiant du voyage concerné par la réservation. */
    private int id_voyage;
    
    /** Le nombre de places réservées. */
    private int nombrePlaces;
    
    /** La date de la réservation. */
    private String dateReservation;
    
    /** Le statut de la réservation (Confirmée, annulée, en attente). */
    private String statut;
    
    /** Le montant payé pour la réservation. */
    private double montantPaye;

    /**
     * Constructeur de la classe Reservation.
     *
     * @param id L'identifiant unique de la réservation.
     * @param idVoyageur L'identifiant du voyageur.
     * @param id_voyage L'identifiant du voyage.
     * @param nombrePlaces Le nombre de places réservées.
     * @param dateReservation La date de la réservation.
     * @param statut Le statut de la réservation.
     * @param montantPaye Le montant payé pour la réservation.
     */
    public Reservation(int id, int idVoyageur, int id_voyage, int nombrePlaces, String dateReservation, String statut, double montantPaye) {
        this.id = id;
        this.idVoyageur = idVoyageur;
        this.id_voyage = id_voyage;
        this.nombrePlaces = nombrePlaces;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.montantPaye = montantPaye;
    }

    /**
     * Retourne l'identifiant de la réservation.
     * @return L'identifiant de la réservation.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant de la réservation.
     * @param id Le nouvel identifiant de la réservation.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne l'identifiant du voyageur ayant effectué la réservation.
     * @return L'identifiant du voyageur.
     */
    public int getIdClient() {
        return idVoyageur;
    }

    /**
     * Définit l'identifiant du voyageur.
     * @param idVoyageur Le nouvel identifiant du voyageur.
     */
    public void setIdClient(int idVoyageur) {
        this.idVoyageur = idVoyageur;
    }

    /**
     * Retourne l'identifiant du voyage concerné par la réservation.
     * @return L'identifiant du voyage.
     */
    public int getid_voyage() {
        return id_voyage;
    }

    /**
     * Définit l'identifiant du voyage.
     * @param id_voyage Le nouvel identifiant du voyage.
     */
    public void setid_voyage(int id_voyage) {
        this.id_voyage = id_voyage;
    }

    /**
     * Retourne le nombre de places réservées.
     * @return Le nombre de places réservées.
     */
    public int getNombrePlaces() {
        return nombrePlaces;
    }

    /**
     * Définit le nombre de places réservées.
     * @param nombrePlaces Le nouveau nombre de places réservées.
     */
    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    /**
     * Retourne la date de la réservation.
     * @return La date de la réservation.
     */
    public String getDateReservation() {
        return dateReservation;
    }

    /**
     * Définit la date de la réservation.
     * @param dateReservation La nouvelle date de la réservation.
     */
    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    /**
     * Retourne le statut de la réservation.
     * @return Le statut de la réservation.
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit le statut de la réservation.
     * @param statut Le nouveau statut de la réservation.
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * Retourne le montant payé pour la réservation.
     * @return Le montant payé.
     */
    public double getMontantPaye() {
        return montantPaye;
    }

    /**
     * Définit le montant payé pour la réservation.
     * @param montantPaye Le nouveau montant payé.
     */
    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }
}