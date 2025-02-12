import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

/**
 * Cette classe représente la fenêtre de connexion de l'application.
 * Elle permet à l'utilisateur de se connecter en utilisant son email et mot de passe.
 */
public class ConnexionFenetre extends JDialog {

    /**
     * Constructeur de la fenêtre de connexion.
     * Ce constructeur crée et configure tous les composants nécessaires de la fenêtre de connexion.
     * 
     * @param parent La fenêtre parente qui sera utilisée pour centrer cette fenêtre.
     */
    public ConnexionFenetre(JFrame parent) {
        super(parent, "Connexion", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);

        // Panneau principal avec du padding
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
        });

        // Panneau d'en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Titre de la fenêtre
        JLabel titleLabel = new JLabel("Log In", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Champ de saisie de l'email
        final JLabel userLabel = new JLabel("Email");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        final JTextField userField = new JTextField(20);
        userField.setPreferredSize(new Dimension(300, 35));

        // Champ de saisie du mot de passe
        final JLabel passLabel = new JLabel("Mot de passe");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        final JPasswordField passField = new JPasswordField(20);
        passField.setPreferredSize(new Dimension(300, 35));

        // Bouton de connexion
        JButton loginButton = new JButton("CONNEXION");
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setBackground(new Color(0, 157, 224));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);

        // Section d'inscription
        JPanel signUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpPanel.setBackground(Color.WHITE);
        JLabel notMemberLabel = new JLabel("Devenir un membre !");
        notMemberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton signUpButton = new JButton("INSCRIPTION");
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setForeground(new Color(0, 157, 224));

        signUpPanel.add(notMemberLabel);
        signUpPanel.add(signUpButton);

        // Ajouter les composants au panneau principal
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(userLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(userField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(passLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(passField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(signUpPanel);

        // Centrer tous les composants
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JComponent) {
                ((JComponent) comp).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }

        // Action listener pour le bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            /**
             * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton de connexion.
             * Elle récupère les données saisies, puis tente d'authentifier l'utilisateur.
             * 
             * @param e L'événement qui se produit lorsque le bouton est cliqué.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = userField.getText();
                String motDePasse = new String(passField.getPassword());

                try {
                    VoyageurDAO voyageurDAO = new VoyageurDAO();
                    final Voyageur voyageur = voyageurDAO.authentifier(email, motDePasse);

                    if (voyageur != null && "admin@gmail.com".equals(voyageur.getEmail()) && "123456".equals(voyageur.getMotDePasse())) {
                        SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
							    dispose();
							    AdminAjout adminAjout = new AdminAjout(voyageur);
							    adminAjout.setVisible(true);
							}
						});
                    } else if (voyageur != null) {
                        SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
							    dispose();
							    VoyageFenetre voyageFenetre = new VoyageFenetre(voyageur);
							    voyageFenetre.setVisible(true);
							}
						});
                    } else {
                        JOptionPane.showMessageDialog(ConnexionFenetre.this, "Utilisateur inexistant", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ConnexionFenetre.this, "Erreur lors de la connexion: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(mainPanel);
        setUndecorated(true);
        setVisible(true);
    }
}