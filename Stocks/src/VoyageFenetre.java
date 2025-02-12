import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * La classe {@code VoyageFenetre} représente la fenêtre principale permettant aux utilisateurs de rechercher et de réserver des voyages.
 * Elle affiche une liste de voyages disponibles, permet la recherche de voyages selon différents critères et offre la possibilité de réserver un voyage.
 * Cette fenêtre intègre une barre de navigation pour faciliter l'accès à d'autres fonctionnalités de l'application.
 */
public class VoyageFenetre extends JFrame {
    private VoyageDAO voyageDAO;
    private ReservationDAO reservationDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField departField, destinationField, dateDepartField, clientField, placesField;
    private Image backgroundImage;
    private JPanel navBar;
    private Voyageur voyageur;

    /**
     * Constructeur de la classe {@code VoyageFenetre}.
     * Initialise la fenêtre, charge l'image de fond, configure les composants graphiques et les actions associées.
     * @param voyageur L'objet {@code Voyageur} représentant l'utilisateur connecté, peut être {@code null} si aucun utilisateur n'est connecté.
     */
    public VoyageFenetre(Voyageur voyageur) {
        this.voyageur = voyageur;
        try {
            backgroundImage = new ImageIcon("resources/voyage.jpg").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image de fond non trouvée");
        }

        // Panel de fond avec l'image
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            /**
             * Méthode surchargée pour dessiner l'image de fond sur le panel.
             * @param g L'objet {@code Graphics} utilisé pour dessiner.
             */
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Initialisation de la barre de navigation
        navBar = createNavBar();

        // Ajout de la barre de navigation au panel de fond
        backgroundPanel.add(navBar, BorderLayout.NORTH);

        setTitle("Gestion des Voyages");
        setSize(1800, 1600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel gris pour le contenu
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(200, 200, 200, 230));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Connexion à la base de données
        voyageDAO = new VoyageDAO();
        reservationDAO = new ReservationDAO();

        // Style général
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
        UIManager.put("Table.gridColor", new Color(200, 200, 200));

        // Création du tableau
        tableModel = new DefaultTableModel(new String[]{"ID", "Départ", "Destination", "Date Départ", "Date Retour", "Places", "Prix"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de recherche avec FlowLayout horizontal
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
        searchPanel.setOpaque(false);
        departField = new JTextField(18);
        destinationField = new JTextField(18);
        dateDepartField = new JTextField(18);
        JButton searchButton = new JButton("Rechercher");

        searchPanel.add(createLabel("Départ:"));
        searchPanel.add(departField);
        searchPanel.add(createLabel("Destination:"));
        searchPanel.add(destinationField);
        searchPanel.add(createLabel("Date départ:"));
        searchPanel.add(dateDepartField);
        searchPanel.add(createButton(searchButton, new Color(0, 123, 255)));

        // Panel de réservation avec FlowLayout horizontal
        JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 10));
        reservationPanel.setOpaque(false);
        clientField = new JTextField(18);
        placesField = new JTextField(6);
        JButton reserverButton = new JButton("Réserver");
        JButton voirReservationsButton = new JButton("Voir réservations");

        reservationPanel.add(createLabel("E-mail:"));
        reservationPanel.add(clientField);
        reservationPanel.add(createLabel("Nombre de places:"));
        reservationPanel.add(placesField);
        reservationPanel.add(createButton(reserverButton, new Color(0, 123, 255)));
        reservationPanel.add(new JLabel());
        reservationPanel.add(createButton(voirReservationsButton, new Color(0, 123, 255)));

        // Actions des boutons
        searchButton.addActionListener(new ActionListener() {
            /**
             * Action effectuée lors du clic sur le bouton "Rechercher".
             * Appelle la méthode {@link #rechercherVoyages()} pour effectuer la recherche.
             * @param e L'événement d'action.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherVoyages();
            }
        });

        reserverButton.addActionListener(new ActionListener() {
            /**
             * Action effectuée lors du clic sur le bouton "Réserver".
             * Appelle la méthode {@link #reserverVoyage()} pour effectuer la réservation.
             * @param e L'événement d'action.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                reserverVoyage();
            }
        });

        voirReservationsButton.addActionListener(new ActionListener() {
            /**
             * Action effectuée lors du clic sur le bouton "Voir réservations".
             * Ouvre la fenêtre des réservations du client si l'e-mail est valide.
             * @param e L'événement d'action.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientEmail = clientField.getText();
                if (clientEmail.isEmpty()) {
                    JOptionPane.showMessageDialog(VoyageFenetre.this, 
                        "Veuillez entrer un e-mail pour consulter les réservations.", 
                        "Erreur", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int voyageurId = reservationDAO.obtenirIdVoyageurParEmail(clientEmail);

                if (voyageurId == -1) {
                    JOptionPane.showMessageDialog(VoyageFenetre.this, 
                        "Aucun voyageur trouvé avec cet e-mail.", 
                        "Erreur", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                new ReservationFenetre(voyageurId);
            }
        });

        // Charger les voyages
        chargerVoyages();

        // Assembler les composants dans le panel de contenu gris
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(reservationPanel, BorderLayout.SOUTH);

        // Ajouter le panel de contenu gris au panel de fond
        backgroundPanel.add(Box.createHorizontalStrut(80), BorderLayout.WEST);
        backgroundPanel.add(Box.createHorizontalStrut(80), BorderLayout.EAST);
        backgroundPanel.add(Box.createVerticalStrut(80), BorderLayout.NORTH);
        backgroundPanel.add(Box.createVerticalStrut(80), BorderLayout.SOUTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        // Configurer la fenêtre
        setContentPane(backgroundPanel);
        setVisible(true);
    }


    /**
     * Crée et configure la barre de navigation avec un bouton de déconnexion.
     * @return Le JPanel représentant la barre de navigation.
     */
     private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(51, 51, 51));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        leftPanel.setOpaque(false);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); // Reduced vertical gap
        rightPanel.setOpaque(false);

        // Deconnexion Button
        JButton btnDeconnexion = createNavButton("Déconnexion");

        btnDeconnexion.addActionListener(new ActionListener() {
            /**
             * Action effectuée lors du clic sur le bouton de déconnexion.
             * Ouvre la fenêtre d'accueil et ferme la fenêtre actuelle.
             * @param e L'événement d'action.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                new PageAcceuilFenetre();  // Ouvre PageAcceuilFenetre
                dispose();              // Ferme VoyageFenetre
            }
        });

        // Add the logout button to the navigation bar
        rightPanel.add(btnDeconnexion);

        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }


    /**
     * Crée un JLabel avec un style spécifique (police Segoe UI Bold 14).
     * @param text Le texte à afficher dans le JLabel.
     * @return Le JLabel avec le style appliqué.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    /**
     * Crée un JTextField avec un style spécifique (police Segoe UI Plain 14 et bordure).
     * @param textField Le JTextField à styliser.
     * @return Le JTextField avec le style appliqué.
     */
    private JTextField createTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    /**
     * Crée un JButton avec un style spécifique (police, couleur de fond, couleur de texte, etc.).
     * @param button Le JButton à styliser.
     * @param color La couleur de fond du bouton.
     * @return Le JButton avec le style appliqué.
     */
    private JButton createButton(final JButton button, final Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Action effectuée lorsque la souris entre dans la zone du bouton.
             * Change la couleur de fond du bouton.
             * @param evt L'événement de souris.
             */
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 105, 217));
            }

            /**
             * Action effectuée lorsque la souris sort de la zone du bouton.
             * Rétablit la couleur de fond du bouton.
             * @param evt L'événement de souris.
             */
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    /**
     * Charge la liste de voyages depuis la base de données et met à jour le modèle de la table.
     */
    private void chargerVoyages() {
        List<Voyage> voyages = voyageDAO.obtenirTousLesVoyages();
        tableModel.setRowCount(0);
        for (Voyage v : voyages) {
            tableModel.addRow(new Object[]{
                v.getId(), 
                v.getDepart(), 
                v.getDestination(), 
                v.getDateDepart(), 
                v.getDateRetour(), 
                v.getPlacesDisponibles(), 
                v.getPrix()
            });
        }
    }

    /**
     * Recherche les voyages en fonction des critères spécifiés (lieu de départ, destination, date de départ).
     */
    private void rechercherVoyages() {
        String depart = departField.getText();
        String destination = destinationField.getText();
        String dateDepart = dateDepartField.getText();
        List<Voyage> voyages = voyageDAO.rechercherVoyages(depart, destination, dateDepart);

        tableModel.setRowCount(0);
        for (Voyage v : voyages) {
            tableModel.addRow(new Object[]{
                v.getId(), 
                v.getDepart(), 
                v.getDestination(), 
                v.getDateDepart(), 
                v.getDateRetour(), 
                v.getPlacesDisponibles(), 
                v.getPrix()
            });
        }
    }

    /**
     * Effectue la réservation d'un voyage pour un client spécifié, en vérifiant la disponibilité des places.
     */
    private void reserverVoyage() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Sélectionnez un voyage.", 
                    "Erreur", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int voyageId = (int) tableModel.getValueAt(selectedRow, 0);
            String clientEmail = clientField.getText();
            int places = Integer.parseInt(placesField.getText());

            if (reservationDAO.reserverVoyage(voyageId, clientEmail, places)) {
                JOptionPane.showMessageDialog(this, 
                    "Réservation réussie !", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                chargerVoyages();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Pas assez de places disponibles.", 
                    "Erreur", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la réservation", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crée un JButton avec un style spécifique pour la barre de navigation (police, couleur de fond, couleur de texte, etc.).
     * @param text Le texte à afficher sur le bouton.
     * @return Le JButton avec le style appliqué.
     */
    private JButton createNavButton(String text) {
        final JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 30));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFont(new Font("Arial", Font.BOLD, 14));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Action effectuée lorsque la souris entre dans la zone du bouton.
             * Change la couleur de fond du bouton.
             * @param evt L'événement de souris.
             */
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }

            /**
             * Action effectuée lorsque la souris sort de la zone du bouton.
             * Rétablit la couleur de fond du bouton.
             * @param evt L'événement de souris.
             */
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }
    /**
     * Méthode principale pour tester la fenêtre des voyages.
     * 
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[]args) {
    	new VoyageFenetre(null);
    }
}