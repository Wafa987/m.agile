import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Fenêtre principale d'accueil de l'application de l'agence de voyage.
 * Elle gère l'affichage de l'image et les boutons de navigation.
 */
public class PageAcceuilFenetre extends JFrame {
    private JLabel imageLabel;  // Label pour afficher l'image
    private ImageIcon originalImageIcon;  // Image d'origine à afficher

    /**
     * Constructeur pour initialiser la fenêtre d'accueil.
     */
    public PageAcceuilFenetre() {
        // Configuration de la fenêtre
        setTitle("Agence de Voyage - Accueil");
        setSize(1800, 1600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création du panel principal avec un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Création de la barre de navigation
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(51, 51, 51));

        // Création des panneaux pour aligner les boutons
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        leftPanel.setOpaque(false);  // Panneau transparent
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        rightPanel.setOpaque(false);  // Panneau transparent

        // Création des boutons de navigation
        JButton btnRecherche = createNavButton("Chercher Voyage");
        JButton btnConnexion = createNavButton("Connexion");
        JButton btnInscription = createNavButton("Inscription");

        // Ajout des boutons aux panneaux correspondants
        leftPanel.add(btnRecherche);
        rightPanel.add(btnConnexion);
        rightPanel.add(btnInscription);

        // Ajout des panneaux à la barre de navigation
        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

        // Chargement de l'image
        originalImageIcon = new ImageIcon("../voyage.jpg");
       

        // Vérifie si l'image existe
        if (originalImageIcon.getIconWidth() == -1) {
            imageLabel = new JLabel("Image non trouvée", SwingConstants.CENTER);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        } else {
            imageLabel = new JLabel();
            updateImageSize();  // Met à jour la taille de l'image selon la taille de la fenêtre
        }

        // Ajout des composants au panel principal
        mainPanel.add(navBar, BorderLayout.NORTH);
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        // Ajout du panel principal à la fenêtre
        setContentPane(mainPanel);

        // Ajouter un écouteur pour redimensionner l'image lorsque la fenêtre change de taille
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateImageSize();
            }
        });

        // Rendre la fenêtre visible
        setVisible(true);
    }

    /**
     * Met à jour la taille de l'image en fonction de la taille actuelle de la fenêtre.
     */
    private void updateImageSize() {
        if (originalImageIcon != null && originalImageIcon.getIconWidth() > 0) {
            int width = getWidth();
            int height = getHeight() - 40; // Compense la hauteur de la barre de navigation
            Image scaledImage = originalImageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        }
    }

    /**
     * Crée un bouton de navigation avec les paramètres définis.
     * @param text Le texte à afficher sur le bouton.
     * @return Le bouton configuré.
     */
    private JButton createNavButton(String text) {
        final JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        
        // Définir une taille plus grande pour le bouton
        button.setPreferredSize(new Dimension(220, 30)); // Largeur : 220 px, Hauteur : 30 px
        
        // Augmenter la marge intérieure du texte
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Changer la taille de la police
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Police plus grande

        // Effet au survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        
        // Ajouter un ActionListener pour ouvrir la fenêtre ConnexionFenetre
        if (text.equals("Connexion")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ConnexionFenetre(PageAcceuilFenetre.this); // Ouvrir la nouvelle fenêtre
                    dispose();  // Ferme la fenêtre actuelle
                }
            });
        }
        
        // Ajouter un ActionListener pour ouvrir la fenêtre InscriptionFenetre
        if (text.equals("Inscription")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new InscriptionFenetre(PageAcceuilFenetre.this); // Ouvrir la nouvelle fenêtre
                    dispose();  // Ferme la fenêtre actuelle
                }
            });
        }

        return button;
    }

    /**
     * Point d'entrée principal pour démarrer l'application.
     * @param args Arguments de ligne de commande.
     */
    public static void main(String[] args) {
        new PageAcceuilFenetre();
    }
}
