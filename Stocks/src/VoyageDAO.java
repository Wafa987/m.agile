import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour gérer les opérations sur la table des voyages dans la base de données.
 * Elle fournit des méthodes pour ajouter, récupérer, mettre à jour, supprimer et rechercher des voyages.
 */
public class VoyageDAO {
    
    // Paramètres de connexion à la base de données
    static final String URL = "jdbc:mysql://localhost:3306/bdagile";
    static final String LOGIN = "root";
    static final String PASS = "";

    /**
     * Constructeur de la classe VoyageDAO.
     * Il charge le pilote JDBC MySQL nécessaire pour se connecter à la base de données.
     */
    public VoyageDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Impossible de charger le pilote MySQL.");
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un voyage dans la base de données.
     * 
     * @param voyage Le voyage à ajouter.
     * @return Le nombre de lignes affectées par l'insertion, 0 si l'insertion échoue.
     */
    public int ajouter(Voyage voyage) {
        String query = "INSERT INTO voyage (depart, description, destination, date_depart, date_retour, places_disponibles, prix) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, voyage.getDepart());
            ps.setString(2, voyage.getDescription());
            ps.setString(3, voyage.getDestination());
            ps.setString(4, voyage.getDateDepart());
            ps.setString(5, voyage.getDateRetour());
            ps.setInt(6, voyage.getPlacesDisponibles());
            ps.setDouble(7, voyage.getPrix());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Récupère un voyage à partir de son ID.
     * 
     * @param id L'ID du voyage à récupérer.
     * @return Le voyage correspondant à l'ID, ou null si aucun voyage n'est trouvé.
     */
    public Voyage getVoyage(int id) {
        String query = "SELECT * FROM voyage WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Voyage(
                        rs.getInt("id"),
                        rs.getString("depart"),
                        rs.getString("description"),
                        rs.getString("destination"),
                        rs.getString("date_depart"),
                        rs.getString("date_retour"),
                        rs.getInt("places_disponibles"),
                        rs.getDouble("prix")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère une liste de tous les voyages dans la base de données.
     * 
     * @return Une liste contenant tous les voyages.
     */
    public List<Voyage> getListeVoyages() {
        List<Voyage> voyages = new ArrayList<>();
        String query = "SELECT * FROM voyage";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                voyages.add(new Voyage(
                        rs.getInt("id"),
                        rs.getString("depart"),
                        rs.getString("description"),
                        rs.getString("destination"),
                        rs.getString("date_depart"),
                        rs.getString("date_retour"),
                        rs.getInt("places_disponibles"),
                        rs.getDouble("prix")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }

    /**
     * Supprime un voyage et ses réservations associées.
     * Utilise une transaction pour s'assurer que la suppression des réservations et du voyage
     * se fait de manière atomique.
     * 
     * @param id L'ID du voyage à supprimer.
     * @return Le nombre de lignes affectées par la suppression, 0 si la suppression échoue.
     */
    public int supprimer(int id) {
        String deleteReservationsQuery = "DELETE FROM reservation WHERE id_voyage = ?";
        String deleteVoyageQuery = "DELETE FROM voyage WHERE id = ?";

        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS)) {
            con.setAutoCommit(false); // Début de la transaction

            // Suppression des réservations liées au voyage
            try (PreparedStatement psReservations = con.prepareStatement(deleteReservationsQuery)) {
                psReservations.setInt(1, id);
                psReservations.executeUpdate();
            }

            // Suppression du voyage
            try (PreparedStatement psVoyage = con.prepareStatement(deleteVoyageQuery)) {
                psVoyage.setInt(1, id);
                int rowsAffected = psVoyage.executeUpdate();

                if (rowsAffected > 0) {
                    // Validation de la transaction si tout s'est bien passé
                    con.commit();
                    return rowsAffected;
                } else {
                    // Si la suppression du voyage échoue, on effectue un rollback
                    con.rollback();
                    return 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS)) {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * Met à jour les informations d'un voyage existant dans la base de données.
     * 
     * @param voyage Le voyage contenant les nouvelles informations.
     * @return Le nombre de lignes affectées par la mise à jour, 0 si la mise à jour échoue.
     */
    public int mettreAJour(Voyage voyage) {
        String query = "UPDATE voyage SET depart = ?, description = ?, destination = ?, date_depart = ?, date_retour = ?, places_disponibles = ?, prix = ? WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, voyage.getDepart());
            ps.setString(2, voyage.getDescription());
            ps.setString(3, voyage.getDestination());
            ps.setString(4, voyage.getDateDepart());
            ps.setString(5, voyage.getDateRetour());
            ps.setInt(6, voyage.getPlacesDisponibles());
            ps.setDouble(7, voyage.getPrix());
            ps.setInt(8, voyage.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Recherche des voyages en fonction du lieu de départ, de la destination et de la date de départ.
     * 
     * @param depart Le lieu de départ à rechercher.
     * @param destination La destination à rechercher.
     * @param dateDepart La date de départ à partir de laquelle rechercher.
     * @return Une liste de voyages correspondant aux critères de recherche.
     */
    public List<Voyage> rechercherVoyages(String depart, String destination, String dateDepart) {
        List<Voyage> voyages = new ArrayList<>();
        String sql = "SELECT * FROM voyage WHERE depart LIKE ? AND destination LIKE ? AND date_depart >= ?";
        
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + depart + "%");
            ps.setString(2, "%" + destination + "%");
            ps.setString(3, dateDepart);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    voyages.add(new Voyage(
                            rs.getInt("id"),
                            rs.getString("depart"),
                            rs.getString("description"),
                            rs.getString("destination"),
                            rs.getString("date_depart"),
                            rs.getString("date_retour"),
                            rs.getInt("places_disponibles"),
                            rs.getDouble("prix")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }

    /**
     * Récupère tous les voyages dans la base de données.
     * 
     * @return Une liste contenant tous les voyages.
     */
    public List<Voyage> obtenirTousLesVoyages() {
        List<Voyage> voyages = new ArrayList<>();
        String sql = "SELECT * FROM voyage";

        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                voyages.add(new Voyage(
                        rs.getInt("id"),
                        rs.getString("depart"),
                        rs.getString("description"),
                        rs.getString("destination"),
                        rs.getString("date_depart"),
                        rs.getString("date_retour"),
                        rs.getInt("places_disponibles"),
                        rs.getDouble("prix")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }
}
