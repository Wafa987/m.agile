import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code Voyageur} représente un voyageur avec ses informations personnelles et son historique de réservations.
 * Elle contient des attributs pour l'ID, le nom, le prénom, l'email, le téléphone, le mot de passe et l'historique des réservations du voyageur.
 * 
 * <p>Cette classe permet de créer un voyageur et de gérer ses informations à travers des méthodes d'accès et de modification.
 * Le mot de passe devrait être stocké sous une forme cryptée pour des raisons de sécurité.
 * </p>
 */
public class Voyageur {
    
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String motDePasse; // Devrait être stocké sous forme cryptée
    private List<Reservation> historiqueReservations;

    /**
     * Constructeur pour créer un voyageur avec toutes les informations nécessaires.
     *
     * @param id L'identifiant unique du voyageur.
     * @param nom Le nom du voyageur.
     * @param prenom Le prénom du voyageur.
     * @param email L'adresse email du voyageur.
     * @param telephone Le numéro de téléphone du voyageur.
     * @param motDePasse Le mot de passe du voyageur (devrait être crypté).
     */
    public Voyageur(int id, String nom, String prenom, String email, String telephone, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.historiqueReservations = new ArrayList<>();
    }

    /**
     * Constructeur pour créer un voyageur sans l'identifiant (utilisé lors de l'inscription).
     *
     * @param nom Le nom du voyageur.
     * @param prenom Le prénom du voyageur.
     * @param email L'adresse email du voyageur.
     * @param telephone Le numéro de téléphone du voyageur.
     * @param motDePasse Le mot de passe du voyageur (devrait être crypté).
     */
    public Voyageur(String nom, String prenom, String email, String telephone, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.historiqueReservations = new ArrayList<>();
    }

    // Getters et Setters

    /**
     * Obtient l'identifiant du voyageur.
     *
     * @return L'identifiant unique du voyageur.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant du voyageur.
     *
     * @param id L'identifiant unique du voyageur.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nom du voyageur.
     *
     * @return Le nom du voyageur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du voyageur.
     *
     * @param nom Le nom du voyageur.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient le prénom du voyageur.
     *
     * @return Le prénom du voyageur.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom du voyageur.
     *
     * @param prenom Le prénom du voyageur.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Obtient l'adresse email du voyageur.
     *
     * @return L'email du voyageur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email du voyageur.
     *
     * @param email L'email du voyageur.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtient le numéro de téléphone du voyageur.
     *
     * @return Le numéro de téléphone du voyageur.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit le numéro de téléphone du voyageur.
     *
     * @param telephone Le numéro de téléphone du voyageur.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Obtient le mot de passe du voyageur.
     *
     * @return Le mot de passe du voyageur (devrait être crypté).
     */
    public String getMotDePasse() {
        return motDePasse;
    }

    /**
     * Définit le mot de passe du voyageur.
     *
     * @param motDePasse Le mot de passe du voyageur (devrait être crypté).
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    /**
     * Obtient l'historique des réservations du voyageur.
     *
     * @return La liste des réservations du voyageur.
     */
    public List<Reservation> getHistoriqueReservations() {
        return historiqueReservations;
    }

    /**
     * Définit l'historique des réservations du voyageur.
     *
     * @param historiqueReservations La liste des réservations à définir.
     */
    public void setHistoriqueReservations(List<Reservation> historiqueReservations) {
        this.historiqueReservations = historiqueReservations;
    }
}
