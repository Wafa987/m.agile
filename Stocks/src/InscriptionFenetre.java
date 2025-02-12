import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre d'inscription qui permet à un utilisateur de s'inscrire en remplissant un formulaire.
 * Elle permet de saisir un nom, prénom, email, mot de passe, et numéro de téléphone.
 * Après la soumission du formulaire, l'utilisateur sera ajouté à la base de données.
 */
public class InscriptionFenetre extends JDialog {

    /**
     * Constructeur pour créer la fenêtre d'inscription.
     * 
     * @param parent Le parent JFrame qui servira de référence pour la position de la fenêtre.
     */
    public InscriptionFenetre(JFrame parent) {
        super(parent, "Inscription", true);
        setSize(400, 650);  // Augmenter la taille pour s'adapter aux nouveaux champs
        setLocationRelativeTo(parent);

        // Panel principal avec padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(Color.WHITE);

        // Bouton de fermeture en haut à droite
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 20));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});  // Ferme la fenêtre

        // Panel pour l'en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Label du titre
        JLabel titleLabel = new JLabel("Inscription", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Champs de saisie pour les informations de l'utilisateur
        final JLabel lastnameLabel = new JLabel("Nom");
        lastnameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        final JTextField lastnameField = new JTextField(20);
        lastnameField.setPreferredSize(new Dimension(300, 35));

        final JLabel prenomLabel = new JLabel("Prenom");
        prenomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        final JTextField prenomField = new JTextField(20);
        prenomField.setPreferredSize(new Dimension(300, 35));

        final JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        final JTextField emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(300, 35));

        final JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        final JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 35));

        final JLabel phoneLabel = new JLabel("Téléphone");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        final JTextField phoneField = new JTextField(20);
        phoneField.setPreferredSize(new Dimension(300, 35));

        // Bouton d'inscription
        JButton signUpButton = new JButton("S'INSCRIRE");
        signUpButton.setPreferredSize(new Dimension(300, 40));
        signUpButton.setBackground(new Color(0, 157, 224));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setBorderPainted(false);
        signUpButton.setFocusPainted(false);

        // Ajout des composants dans le panel principal
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(lastnameLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(lastnameField);

        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(prenomLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(prenomField);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(emailLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(emailField);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(passwordField);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(phoneLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(phoneField);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(signUpButton);

        // Centrer tous les composants
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JComponent) {
                ((JComponent) comp).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }

        // Action listener pour le bouton d'inscription
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer les informations saisies
                String name = prenomField.getText();
                String lastname = lastnameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String phone = phoneField.getText();

                try {
                    // Créer une instance de VoyageurDAO pour l'inscription
                    VoyageurDAO voyageurDAO = new VoyageurDAO();

                    // Appeler la méthode inscrire pour ajouter le nouvel utilisateur
                    Voyageur voyageur = new Voyageur(lastname, name, email, phone, password);
                    int inscriptionReussie = voyageurDAO.ajouter(voyageur);

                    if (inscriptionReussie == 1) {
                        JOptionPane.showMessageDialog(InscriptionFenetre.this,
                            "Inscription réussie ! Vous pouvez vous connecter.",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                        dispose();  // Fermer la fenêtre d'inscription après succès
                    } else {
                        JOptionPane.showMessageDialog(InscriptionFenetre.this,
                            "Une erreur est survenue lors de l'inscription.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(InscriptionFenetre.this,
                        "Erreur lors de l'inscription: " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajouter le panel principal au dialogue
        add(mainPanel);

        setUndecorated(true); // Enlever les décorations de la fenêtre
        setVisible(true);  // Rendre la fenêtre visible
    }

    /**
     * Méthode principale pour tester la fenêtre d'inscription.
     * 
     * @param args Arguments de ligne de commande.
     */
    public static void main(String[] args) {
        // Pour les tests
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new InscriptionFenetre(frame);
    }
}
