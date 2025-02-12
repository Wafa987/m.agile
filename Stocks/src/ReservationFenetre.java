import javax.swing.*; // Importation des classes pour l'interface graphique Swing
import javax.swing.table.DefaultTableModel; // Importation de la classe pour manipuler les modèles de table
import java.awt.*; // Importation des classes pour la mise en page et les éléments graphiques
import java.awt.event.ActionEvent; // Importation des événements d'action
import java.awt.event.ActionListener; // Importation de l'interface pour écouter les événements d'action
import java.util.List; // Importation de la classe List pour manipuler des collections

/**
 * Fenêtre graphique permettant de gérer les réservations d'un voyageur.
 * Cette classe affiche les réservations d'un voyageur et permet d'annuler une réservation.
 * 
 * @author Assil Dekhil
 */
public class ReservationFenetre extends JFrame {
    private int idVoyageur; // Identifiant du voyageur dont on gère les réservations
    private JTable table; // Table affichant les réservations
    private DefaultTableModel tableModel; // Modèle de données pour la table
    private ReservationDAO reservationDAO; // Objet pour interagir avec la base de données des réservations
    private VoyageDAO voyageDAO; // Objet pour interagir avec la base de données des voyages

    /**
     * Constructeur de la fenêtre de réservation.
     * 
     * @param idVoyageur L'ID du voyageur pour lequel afficher les réservations.
     */
    public ReservationFenetre(int idVoyageur) {
        this.idVoyageur = idVoyageur;
        reservationDAO = new ReservationDAO();
        voyageDAO = new VoyageDAO(); // Initialisation du DAO pour les voyages

        setTitle("Réservations du Voyageur ID: " + idVoyageur); // Titre de la fenêtre
        setSize(1000, 600); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Comportement de fermeture de la fenêtre
        setLocationRelativeTo(null); // Centrage de la fenêtre

        setLayout(new BorderLayout(10, 10)); // Mise en page avec un espacement de 10px
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE); // Personnalisation du bouton par défaut

        // Initialisation du modèle de la table pour afficher les informations des réservations
        tableModel = new DefaultTableModel(
            new String[]{"ID Réservation", "Départ", "Destination", "Date Départ", "Date Réservation", "Nombre de places"}, 
            0
        );
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Style de la police pour le tableau
        table.setRowHeight(30); // Hauteur des lignes
        table.setGridColor(new Color(200, 200, 200)); // Couleur de la grille
        table.setShowGrid(true); // Affichage de la grille
        table.setIntercellSpacing(new Dimension(0, 0)); // Espacement entre les cellules

        JScrollPane scrollPane = new JScrollPane(table); // Ajout de la table dans un panneau défilable
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Bordure du panneau
        add(scrollPane, BorderLayout.CENTER); // Ajout du panneau au centre de la fenêtre

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panneau en bas de la fenêtre
        JButton cancelButton = new JButton("Annuler réservation"); // Bouton pour annuler la réservation
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Style du bouton
        cancelButton.setBackground(new Color(220, 53, 69)); // Couleur de fond du bouton
        cancelButton.setForeground(Color.WHITE); // Couleur du texte du bouton
        cancelButton.setFocusPainted(false); // Suppression de la mise en surbrillance du bouton
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur sous forme de main
        bottomPanel.add(cancelButton); // Ajout du bouton au panneau inférieur
        add(bottomPanel, BorderLayout.SOUTH); // Ajout du panneau au bas de la fenêtre

        chargerReservations(); // Chargement des réservations dans la table

        // Action du bouton pour annuler une réservation
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                annulerReservation();
            }
        });

        setVisible(true); // Affichage de la fenêtre
    }

    /**
     * Charge les réservations du voyageur dans la table.
     * Cette méthode récupère les réservations via le DAO et les affiche dans le tableau.
     */
    private void chargerReservations() {
        List<Reservation> reservations = reservationDAO.obtenirReservationsParClient(idVoyageur); // Récupération des réservations
        tableModel.setRowCount(0); // Réinitialisation du modèle de table
        
        for (Reservation r : reservations) {
            // Récupérer les informations du voyage associé à la réservation
            Voyage voyage = voyageDAO.getVoyage(r.getid_voyage());
            
            if (voyage != null) {
                // Ajout des informations du voyage et de la réservation dans le tableau
                tableModel.addRow(new Object[]{
                    r.getId(),
                    voyage.getDepart(),
                    voyage.getDestination(),
                    voyage.getDateDepart(),
                    r.getDateReservation(),
                    r.getNombrePlaces()
                });
            }
        }
    }

    /**
     * Annule une réservation sélectionnée dans la table.
     * Cette méthode demande confirmation avant d'annuler la réservation.
     */
    private void annulerReservation() {
        int selectedRow = table.getSelectedRow(); // Récupère la ligne sélectionnée dans la table
        if (selectedRow == -1) {
            // Si aucune ligne n'est sélectionnée, affiche un message d'erreur
            JOptionPane.showMessageDialog(this, 
                "Sélectionnez une réservation à annuler.", 
                "Erreur", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int reservationId = (int) tableModel.getValueAt(selectedRow, 0); // Récupère l'ID de la réservation

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir annuler cette réservation ?", 
            "Annulation", 
            JOptionPane.YES_NO_OPTION); // Demande de confirmation à l'utilisateur
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Si la réservation est annulée avec succès, recharge les réservations
            if (reservationDAO.annulerReservation(reservationId)) {
                JOptionPane.showMessageDialog(this, 
                    "Réservation annulée avec succès.", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                chargerReservations();
            } else {
                // Si une erreur survient lors de l'annulation, affiche un message d'erreur
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'annulation de la réservation.", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Méthode principale pour lancer l'application.
     * 
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Création de la fenêtre pour un voyageur avec l'ID 1
                new ReservationFenetre(1);
            }
        });
    }
}
