import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Fenêtre principale pour la gestion des voyageurs dans l'application.
 * Permet d'ajouter, afficher et supprimer des voyageurs à l'aide d'une interface graphique.
 * Utilise un modèle de conception basé sur JFrame et les actions sont traitées par l'implémentation d'ActionListener.
 */
public class VoyageurFenetre extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField textFieldNom, textFieldPrenom, textFieldEmail, textFieldTelephone, textFieldMotDePasse;
    private JButton boutonAjouter, boutonAfficher, boutonSupprimer;
    private JTextArea zoneTextVoyageurs;
    private JScrollPane zoneDefilement;
    private VoyageurDAO voyageurDAO;

    /**
     * Constructeur de la fenêtre de gestion des voyageurs.
     * Initialise les composants graphiques et configure la fenêtre.
     */
    public VoyageurFenetre() {
        voyageurDAO = new VoyageurDAO();
        
        // Paramètres de la fenêtre
        this.setTitle("Gestion des Voyageurs");
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Création du conteneur principal
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Création des champs de texte
        textFieldNom = new JTextField();
        textFieldPrenom = new JTextField();
        textFieldEmail = new JTextField();
        textFieldTelephone = new JTextField();
        textFieldMotDePasse = new JTextField();
        
        // Création des labels
        containerPanel.add(createLabel("Nom :"));
        containerPanel.add(textFieldNom);
        containerPanel.add(createLabel("Prénom :"));
        containerPanel.add(textFieldPrenom);
        containerPanel.add(createLabel("Email :"));
        containerPanel.add(textFieldEmail);
        containerPanel.add(createLabel("Téléphone :"));
        containerPanel.add(textFieldTelephone);
        containerPanel.add(createLabel("Mot de passe :"));
        containerPanel.add(textFieldMotDePasse);

        // Création des boutons
        boutonAjouter = new JButton("Ajouter Voyageur");
        boutonAfficher = new JButton("Afficher Voyageurs");
        boutonSupprimer = new JButton("Supprimer Voyageur");

        boutonAjouter.addActionListener(this);
        boutonAfficher.addActionListener(this);
        boutonSupprimer.addActionListener(this);

        // Ajout des boutons au conteneur
        containerPanel.add(boutonAjouter);
        containerPanel.add(boutonAfficher);
        containerPanel.add(boutonSupprimer);

        // Zone d'affichage des voyageurs
        zoneTextVoyageurs = new JTextArea(10, 30);
        zoneTextVoyageurs.setEditable(false);
        zoneDefilement = new JScrollPane(zoneTextVoyageurs);
        containerPanel.add(zoneDefilement);

        // Ajout du conteneur à la fenêtre
        this.setContentPane(containerPanel);
        this.setVisible(true);
    }

    /**
     * Crée un label avec le texte spécifié.
     * @param text Le texte à afficher dans le label.
     * @return Un JLabel avec le texte spécifié.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Gestion des événements d'action (boutons).
     * @param e L'événement d'action qui a eu lieu.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonAjouter) {
            ajouterVoyageur();
        } else if (e.getSource() == boutonAfficher) {
            afficherVoyageurs();
        } else if (e.getSource() == boutonSupprimer) {
            supprimerVoyageur();
        }
    }

    /**
     * Ajoute un nouveau voyageur à la base de données.
     * Les informations sont récupérées à partir des champs de texte.
     * Un message est affiché pour indiquer si l'ajout a été effectué avec succès ou non.
     */
    private void ajouterVoyageur() {
        String nom = textFieldNom.getText();
        String prenom = textFieldPrenom.getText();
        String email = textFieldEmail.getText();
        String telephone = textFieldTelephone.getText();
        String motDePasse = textFieldMotDePasse.getText();

        if (isValidInput(nom, prenom, email, telephone, motDePasse)) {
            Voyageur voyageur = new Voyageur(0, nom, prenom, email, telephone, motDePasse);
            int result = voyageurDAO.ajouter(voyageur);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Voyageur ajouté avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du voyageur.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
        }
    }

    /**
     * Vérifie si tous les champs d'entrée sont valides (non vides).
     * @param fields Les champs à vérifier.
     * @return true si tous les champs sont remplis, false sinon.
     */
    private boolean isValidInput(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Affiche la liste des voyageurs dans la zone de texte.
     * Les voyageurs sont récupérés à partir de la base de données.
     */
    private void afficherVoyageurs() {
        List<Voyageur> voyageurs = voyageurDAO.getListeVoyageurs();
        zoneTextVoyageurs.setText(""); // Efface la zone avant d'afficher les nouveaux résultats
        for (Voyageur v : voyageurs) {
            zoneTextVoyageurs.append(v.getId() + " - " + v.getNom() + " " + v.getPrenom() + " - " + v.getEmail() + "\n");
        }
    }

    /**
     * Supprime un voyageur de la base de données en fonction de son ID.
     * Un message est affiché pour indiquer si la suppression a été effectuée avec succès ou non.
     */
    private void supprimerVoyageur() {
        String idString = JOptionPane.showInputDialog(this, "Entrez l'ID du voyageur à supprimer :");
        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                int result = voyageurDAO.supprimer(id);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Voyageur supprimé avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun voyageur trouvé avec cet ID.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID invalide.");
            }
        }
    }

    /**
     * Méthode principale qui lance l'application en affichant la fenêtre de gestion des voyageurs.
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VoyageurFenetre();
            }
        });
    }
}
