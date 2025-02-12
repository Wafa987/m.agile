/**
 * Représente un avis laissé par un voyageur sur un voyage.
 */
public class Avis {
    private int id;
    private int idVoyageur;
    private Voyage voyage;
    private int note; // 1 à 5 étoiles
    private String commentaire;

    /**
     * Constructeur pour créer un avis.
     *
     * @param id          Identifiant unique de l'avis.
     * @param idVoyageur  Identifiant du voyageur ayant laissé l'avis.
     * @param voyage      Le voyage concerné par l'avis.
     * @param note        Note attribuée (entre 1 et 5).
     * @param commentaire Commentaire laissé par le voyageur.
     */
    public Avis(int id, int idVoyageur, Voyage voyage, int note, String commentaire) {
        this.id = id;
        this.idVoyageur = idVoyageur;
        this.voyage = voyage;
        setNote(note); // Utilisation du setter pour valider la note
        this.commentaire = commentaire;
    }

    // Getters

    /**
     * @return L'identifiant unique de l'avis.
     */
    public int getId() {
        return id;
    }

    /**
     * @return L'identifiant du voyageur ayant laissé l'avis.
     */
    public int getIdVoyageur() {
        return idVoyageur;
    }

    /**
     * @return Le voyage concerné par l'avis.
     */
    public Voyage getVoyage() {
        return voyage;
    }

    /**
     * @return La note attribuée au voyage (entre 1 et 5).
     */
    public int getNote() {
        return note;
    }

    /**
     * @return Le commentaire du voyageur.
     */
    public String getCommentaire() {
        return commentaire;
    }

    // Setters

    /**
     * Définit l'identifiant unique de l'avis.
     *
     * @param id L'identifiant unique de l'avis.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Définit l'identifiant du voyageur.
     *
     * @param idVoyageur L'identifiant du voyageur.
     */
    public void setIdVoyageur(int idVoyageur) {
        this.idVoyageur = idVoyageur;
    }

    /**
     * Définit le voyage concerné par l'avis.
     *
     * @param voyage Le voyage concerné.
     */
    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    /**
     * Définit la note attribuée au voyage.
     *
     * @param note La note (doit être entre 1 et 5).
     * @throws IllegalArgumentException si la note est hors de la plage valide.
     */
    public void setNote(int note) {
        if (note >= 1 && note <= 5) {
            this.note = note;
        } else {
            throw new IllegalArgumentException("La note doit être entre 1 et 5.");
        }
    }

    /**
     * Définit le commentaire du voyageur.
     *
     * @param commentaire Le commentaire à associer à l'avis.
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", idVoyageur=" + idVoyageur +
                ", voyage=" + voyage +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}
