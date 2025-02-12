import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code VoyageurDAO} fournit des méthodes pour interagir avec la base de données concernant les voyageurs.
 * Elle permet d'ajouter, récupérer, mettre à jour, supprimer des voyageurs ainsi que d'effectuer l'authentification d'un voyageur.
 */
public class VoyageurDAO {
    
    // URL de la base de données, le login et le mot de passe pour la connexion
    static final String URL = "jdbc:mysql://localhost:3306/bdagile";
    static final String LOGIN = "root";
    static final String PASS = "";

    /**
     * Constructeur qui charge le driver JDBC MySQL.
     * 
     * @throws ClassNotFoundException Si le driver MySQL n'est pas trouvé.
     */
    public VoyageurDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Impossible de charger le pilote MySQL, vérifiez que le fichier .jar est bien importé.");
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un nouveau voyageur dans la base de données.
     * 
     * @param voyageur Le voyageur à ajouter.
     * @return Le nombre de lignes affectées par l'opération (0 ou 1).
     */
    public int ajouter(Voyageur voyageur) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement(
                "INSERT INTO voyageur (nom, prenom, email, telephone, mot_de_passe) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setString(1, voyageur.getNom());
            ps.setString(2, voyageur.getPrenom());
            ps.setString(3, voyageur.getEmail());
            ps.setString(4, voyageur.getTelephone());
            ps.setString(5, voyageur.getMotDePasse());
            retour = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return retour;
    }

    /**
     * Récupère un voyageur en fonction de son ID.
     * 
     * @param id L'ID du voyageur à récupérer.
     * @return Le voyageur correspondant à l'ID ou {@code null} si non trouvé.
     */
    public Voyageur getVoyageur(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Voyageur voyageur = null;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM voyageur WHERE id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                voyageur = new Voyageur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("mot_de_passe")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return voyageur;
    }

    /**
     * Récupère la liste de tous les voyageurs présents dans la base de données.
     * 
     * @return Une liste contenant tous les voyageurs.
     */
    public List<Voyageur> getListeVoyageurs() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Voyageur> voyageurs = new ArrayList<>();

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM voyageur");
            rs = ps.executeQuery();
            while (rs.next()) {
                Voyageur voyageur = new Voyageur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("mot_de_passe")
                );
                voyageurs.add(voyageur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return voyageurs;
    }

    /**
     * Supprime un voyageur de la base de données.
     * 
     * @param id L'ID du voyageur à supprimer.
     * @return Le nombre de lignes affectées par l'opération (0 ou 1).
     */
    public int supprimer(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("DELETE FROM voyageur WHERE id = ?");
            ps.setInt(1, id);
            retour = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return retour;
    }

    /**
     * Met à jour les informations d'un voyageur dans la base de données.
     * 
     * @param voyageur Le voyageur avec les nouvelles informations.
     * @return Le nombre de lignes affectées par l'opération (0 ou 1).
     */
    public int mettreAJour(Voyageur voyageur) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement(
                "UPDATE voyageur SET nom = ?, prenom = ?, email = ?, telephone = ?, mot_de_passe = ? WHERE id = ?"
            );
            ps.setString(1, voyageur.getNom());
            ps.setString(2, voyageur.getPrenom());
            ps.setString(3, voyageur.getEmail());
            ps.setString(4, voyageur.getTelephone());
            ps.setString(5, voyageur.getMotDePasse());
            ps.setInt(6, voyageur.getId());
            retour = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return retour;
    }

    /**
     * Authentifie un voyageur en fonction de son email et mot de passe.
     * 
     * @param email L'email du voyageur.
     * @param motDePasse Le mot de passe du voyageur.
     * @return Le voyageur correspondant à l'email et mot de passe, ou {@code null} si aucun voyageur trouvé.
     */
    public Voyageur authentifier(String email, String motDePasse) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Voyageur voyageur = null;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            // Vérifier si un voyageur avec cet email et mot de passe existe
            ps = con.prepareStatement("SELECT * FROM voyageur WHERE email = ? AND mot_de_passe = ?");
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            rs = ps.executeQuery();

            // Si un voyageur est trouvé, le récupérer
            if (rs.next()) {
                voyageur = new Voyageur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("mot_de_passe")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return voyageur; // Retourne null si aucune correspondance n'est trouvée
    }
}
