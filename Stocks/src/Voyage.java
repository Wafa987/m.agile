/**
 * Classe représentant un voyage.
 * Cette classe contient les informations relatives à un voyage, telles que les lieux de départ et de destination,
 * les dates de départ et de retour, la description du voyage, le nombre de places disponibles et le prix du voyage.
 */
public class Voyage {
    private int id;
    private String depart;
    private String destination;
    private String description;
    private String dateDepart;
    private String dateRetour;
    private int placesDisponibles;
    private double prix;

    /**
     * Constructeur pour initialiser un objet Voyage avec les informations spécifiées.
     * 
     * @param id L'identifiant unique du voyage.
     * @param depart Le lieu de départ du voyage.
     * @param description La description du voyage.
     * @param destination Le lieu de destination du voyage.
     * @param dateDepart La date et l'heure de départ du voyage.
     * @param dateRetour La date et l'heure de retour du voyage.
     * @param placesDisponibles Le nombre de places disponibles pour ce voyage.
     * @param prix Le prix du voyage.
     */
    public Voyage(int id, String depart, String description, String destination, String dateDepart, String dateRetour, int placesDisponibles, double prix) {
        this.id = id;
        this.depart = depart;
        this.description = description;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.placesDisponibles = placesDisponibles;
        this.prix = prix;
    }

    // Getters and Setters

    /**
     * @return L'identifiant unique du voyage.
     */
    public int getId() {
        return id;
    }

    /**
     * Modifie l'identifiant du voyage.
     * 
     * @param id Le nouvel identifiant du voyage.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Le lieu de départ du voyage.
     */
    public String getDepart() {
        return depart;
    }

    /**
     * Modifie le lieu de départ du voyage.
     * 
     * @param depart Le nouveau lieu de départ.
     */
    public void setDepart(String depart) {
        this.depart = depart;
    }

    /**
     * @return La description du voyage.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description du voyage.
     * 
     * @param description La nouvelle description du voyage.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Le lieu de destination du voyage.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Modifie le lieu de destination du voyage.
     * 
     * @param destination Le nouveau lieu de destination.
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return La date et l'heure de départ du voyage.
     */
    public String getDateDepart() {
        return dateDepart;
    }

    /**
     * Modifie la date et l'heure de départ du voyage.
     * 
     * @param dateDepart La nouvelle date et heure de départ.
     */
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    /**
     * @return La date et l'heure de retour du voyage.
     */
    public String getDateRetour() {
        return dateRetour;
    }

    /**
     * Modifie la date et l'heure de retour du voyage.
     * 
     * @param dateRetour La nouvelle date et heure de retour.
     */
    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    /**
     * @return Le nombre de places disponibles pour ce voyage.
     */
    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    /**
     * Modifie le nombre de places disponibles pour ce voyage.
     * 
     * @param placesDisponibles Le nouveau nombre de places disponibles.
     */
    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    /**
     * @return Le prix du voyage.
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Modifie le prix du voyage.
     * 
     * @param prix Le nouveau prix du voyage.
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }
}
