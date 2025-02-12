/**
 * Classe représentant un moyen de transport.
 * Cette classe contient les informations relatives à un transport, telles que le type, 
 * la compagnie, les dates de départ et d'arrivée, les lieux de départ et d'arrivée, la disponibilité,
 * et le prix du transport.
 */
public class Transport {
    private int id;
    private String type; // Avion, train, bus
    private String compagnie;
    private String dateDepart;
    private String dateArrivee;
    private String lieuDepart;
    private String lieuArrivee;
    private int disponibilites;
    private double prix;

    /**
     * Constructeur pour initialiser un objet Transport avec les informations spécifiées.
     * 
     * @param id L'identifiant unique du transport.
     * @param type Le type de transport (Avion, Train, Bus).
     * @param compagnie La compagnie assurant le transport.
     * @param dateDepart La date et l'heure de départ du transport.
     * @param dateArrivee La date et l'heure d'arrivée du transport.
     * @param lieuDepart Le lieu de départ du transport.
     * @param lieuArrivee Le lieu d'arrivée du transport.
     * @param disponibilites Le nombre de places disponibles pour ce transport.
     * @param prix Le prix du transport.
     */
    public Transport(int id, String type, String compagnie, String dateDepart, String dateArrivee, String lieuDepart, String lieuArrivee , int disponibilites, double prix) {
        this.id = id;
        this.type = type;
        this.compagnie = compagnie;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.lieuDepart = lieuDepart;
        this.lieuArrivee = lieuArrivee;
        this.disponibilites = disponibilites;
        this.prix = prix;
    }

    // Getters

    /**
     * @return L'identifiant unique du transport.
     */
    public int getId() {
        return id;
    }

    /**
     * @return Le type du transport (Avion, Train, Bus).
     */
    public String getType() {
        return type;
    }

    /**
     * @return Le nom de la compagnie assurant le transport.
     */
    public String getCompagnie() {
        return compagnie;
    }

    /**
     * @return La date et l'heure de départ du transport.
     */
    public String getDateDepart() {
        return dateDepart;
    }

    /**
     * @return La date et l'heure d'arrivée du transport.
     */
    public String getDateArrivee() {
        return dateArrivee;
    }

    /**
     * @return Le nombre de places disponibles pour ce transport.
     */
    public int getDisponibilites() {
        return disponibilites;
    }

    /**
     * @return Le prix du transport.
     */
    public double getPrix() {
        return prix;
    }

    // Setters

    /**
     * Modifie l'identifiant du transport.
     * 
     * @param id Le nouvel identifiant du transport.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Modifie le type du transport.
     * 
     * @param type Le nouveau type de transport (Avion, Train, Bus).
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Modifie la compagnie assurant le transport.
     * 
     * @param compagnie Le nouveau nom de la compagnie.
     */
    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    /**
     * Modifie la date et l'heure de départ du transport.
     * 
     * @param dateDepart La nouvelle date et heure de départ.
     */
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    /**
     * Modifie la date et l'heure d'arrivée du transport.
     * 
     * @param dateArrivee La nouvelle date et heure d'arrivée.
     */
    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    /**
     * Modifie le nombre de places disponibles pour ce transport.
     * 
     * @param disponibilites Le nouveau nombre de places disponibles.
     */
    public void setDisponibilites(int disponibilites) {
        this.disponibilites = disponibilites;
    }

    /**
     * Modifie le prix du transport.
     * 
     * @param prix Le nouveau prix du transport.
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }

    /**
     * @return Le lieu de départ du transport.
     */
    public String getLieuDepart() {
        return lieuDepart;
    }

    /**
     * Modifie le lieu de départ du transport.
     * 
     * @param lieuDepart Le nouveau lieu de départ.
     */
    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    /**
     * @return Le lieu d'arrivée du transport.
     */
    public String getLieuArrivee() {
        return lieuArrivee;
    }

    /**
     * Modifie le lieu d'arrivée du transport.
     * 
     * @param lieuArrivee Le nouveau lieu d'arrivée.
     */
    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }
}
