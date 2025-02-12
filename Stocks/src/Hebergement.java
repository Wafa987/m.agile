public class Hebergement {
    private int id;
    private String type; // Hôtel, Appartement, etc.
    private String adresse;
    private int capacite;
    private int disponibilites;
    private double prixParNuit;

    public Hebergement(int id, String type, String adresse, int capacite, int disponibilites, double prixParNuit) {
        this.id = id;
        this.type = type;
        this.adresse = adresse;
        this.capacite = capacite;
        this.disponibilites = disponibilites;
        this.prixParNuit = prixParNuit;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getDisponibilites() {
        return disponibilites;
    }

    public double getPrixParNuit() {
        return prixParNuit;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public void setDisponibilites(int disponibilites) {
        this.disponibilites = disponibilites;
    }

    public void setPrixParNuit(double prixParNuit) {
        this.prixParNuit = prixParNuit;
    }
}
