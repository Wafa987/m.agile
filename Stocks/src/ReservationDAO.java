import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * La classe <code>ReservationDAO</code> est responsable de la gestion des opérations de réservation dans la base de données.
 * Elle permet d'ajouter, supprimer, mettre à jour, et récupérer des réservations, ainsi que d'effectuer des actions liées
 * aux voyageurs et aux voyages.
 */
public class ReservationDAO {
    static final String URL = "jdbc:mysql://localhost:3306/bdagile";
    static final String LOGIN = "root";
    static final String PASS = "";

    /**
     * Constructeur de la classe <code>ReservationDAO</code>.
     * Il initialise la connexion à la base de données en chargeant le driver MySQL.
     */
    public ReservationDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Impossible de charger le pilote MySQL.");
            e.printStackTrace();
        }
    }

    /**
     * Ajoute une nouvelle réservation dans la base de données.
     * 
     * @param reservation L'objet <code>Reservation</code> contenant les informations de la réservation.
     * @return Le nombre de lignes affectées dans la base de données (1 si la réservation a été ajoutée, 0 sinon).
     */
    public int ajouterReservation(Reservation reservation) {
        String query = "INSERT INTO reservation (id_voyage, idVoyageur, nombre_places) VALUES (?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, reservation.getid_voyage());
            ps.setInt(2, reservation.getIdClient());
            ps.setInt(3, reservation.getNombrePlaces());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Récupère le nom du client à partir de son ID de voyageur.
     * 
     * @param idVoyageur L'ID du voyageur dont le nom doit être récupéré.
     * @return Le nom du voyageur si trouvé, sinon <code>null</code>.
     */
    public String getNomClient(int idVoyageur) {
        String query = "SELECT nom FROM voyageur WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idVoyageur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Réalise une réservation pour un voyage donné, en vérifiant la disponibilité des places et en enregistrant la réservation.
     * La méthode effectue les actions suivantes dans l'ordre :
     * 1. Récupération de l'ID du client à partir de son email.
     * 2. Vérification et mise à jour des places disponibles pour le voyage.
     * 3. Insertion de la réservation dans la base de données.
     * 
     * @param voyageId L'ID du voyage pour lequel réserver.
     * @param clientEmail L'email du client à réserver.
     * @param nombrePlaces Le nombre de places à réserver.
     * @return <code>true</code> si la réservation a été effectuée avec succès, <code>false</code> sinon.
     */
    public boolean reserverVoyage(int voyageId, String clientEmail, int nombrePlaces) {
        String sqlGetClientId = "SELECT id FROM voyageur WHERE email = ?";
        String sqlUpdateVoyage = "UPDATE voyage SET places_disponibles = places_disponibles - ? WHERE id = ? AND places_disponibles >= ?";
        String sqlInsertReservation = "INSERT INTO reservation (id_voyage, idVoyageur, nombre_places) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS)) {
            con.setAutoCommit(false); // Début de transaction

            // 1. Récupérer l'ID du client via son email
            int clientId = -1;
            try (PreparedStatement psClient = con.prepareStatement(sqlGetClientId)) {
                psClient.setString(1, clientEmail);
                ResultSet rs = psClient.executeQuery();
                if (rs.next()) {
                    clientId = rs.getInt("id");
                } else {
                    JOptionPane.showMessageDialog(null, "Client introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return false; // Arrêt si le client n'existe pas
                }
            }

            // 2. Vérifier et mettre à jour les places disponibles
            try (PreparedStatement psVoyage = con.prepareStatement(sqlUpdateVoyage)) {
                psVoyage.setInt(1, nombrePlaces);
                psVoyage.setInt(2, voyageId);
                psVoyage.setInt(3, nombrePlaces);

                int rowsUpdated = psVoyage.executeUpdate();
                if (rowsUpdated > 0) {
                    // 3. Insérer la réservation avec l'ID du client
                    try (PreparedStatement psReservation = con.prepareStatement(sqlInsertReservation)) {
                        psReservation.setInt(1, voyageId);
                        psReservation.setInt(2, clientId);
                        psReservation.setInt(3, nombrePlaces);
                        psReservation.executeUpdate();
                    }

                    con.commit(); // Valider la transaction
                    return true;
                } else {
                    con.rollback(); // Annuler si pas assez de places
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère toutes les réservations faites par un client donné.
     * 
     * @param idVoyageur L'ID du voyageur dont les réservations doivent être récupérées.
     * @return Une liste d'objets <code>Reservation</code> correspondant aux réservations du client.
     */
    public List<Reservation> obtenirReservationsParClient(int idVoyageur) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation WHERE idVoyageur = ?";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idVoyageur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(new Reservation(
                    rs.getInt("id"),
                    rs.getInt("idVoyageur"),
                    rs.getInt("id_voyage"),
                    rs.getInt("nombre_places"),
                    rs.getString("dateReservation"),
                    rs.getString("statut"),
                    rs.getDouble("montantPaye")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Annule une réservation en la supprimant de la base de données.
     * 
     * @param reservationId L'ID de la réservation à annuler.
     * @return <code>true</code> si la réservation a été annulée avec succès, <code>false</code> sinon.
     */
    public boolean annulerReservation(int reservationId) {
        String query = "DELETE FROM reservation WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, reservationId);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0; // Retourne true si la réservation a été supprimée
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère l'ID d'un voyageur en fonction de son adresse email.
     * 
     * @param email L'email du voyageur dont l'ID doit être récupéré.
     * @return L'ID du voyageur si trouvé, sinon -1.
     */
    public int obtenirIdVoyageurParEmail(String email) {
        int voyageurId = -1; // Retourne -1 si le voyageur n'est pas trouvé
        String query = "SELECT id FROM voyageur WHERE email = ?"; // Requête pour obtenir l'ID du voyageur à partir de l'email

        try (Connection con = DriverManager.getConnection(URL, LOGIN, PASS);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, email); // Remplir le paramètre de l'email
            ResultSet rs = stmt.executeQuery();

            // Vérifier si un résultat est trouvé
            if (rs.next()) {
                voyageurId = rs.getInt("id"); // Récupérer l'ID du voyageur
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs SQL
        }
        
        return voyageurId;
    }
}
