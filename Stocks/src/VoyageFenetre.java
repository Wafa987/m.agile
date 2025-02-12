import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VoyageFenetre extends JFrame {
    private VoyageDAO voyageDAO;
    private ReservationDAO reservationDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField departField, destinationField, dateDepartField, clientField, placesField;
    private Image backgroundImage;
    private JPanel navBar;
    private Voyageur voyageur;

    public VoyageFenetre(Voyageur voyageur) {
        this.voyageur = voyageur;
        try {
            backgroundImage = new ImageIcon("../voyage.jpg").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image de fond non trouvée");
        }

        // Panel de fond avec l'image
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
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
        navBar = new JPanel(new BorderLayout());

        // Création des panneaux pour aligner les boutons
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        leftPanel.setOpaque(false);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        rightPanel.setOpaque(false);

        // Boutons de navigation
        JButton btnRecherche = createNavButton("Chercher Voyage");
        JButton btnConnexion = createNavButton("Connexion");
        JButton btnInscription = createNavButton("Inscription");

        leftPanel.add(btnRecherche);
        rightPanel.add(btnConnexion);
        rightPanel.add(btnInscription);

        // Ajout des panels à la barre de navigation
        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

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
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherVoyages();
            }
        });

        reserverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserverVoyage();
            }
        });

        voirReservationsButton.addActionListener(new ActionListener() {
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

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JTextField createTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JButton createButton(final JButton button, final Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

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

    private JButton createNavButton(String text) {
        final JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Arial", Font.BOLD, 18));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }
    public static void main(String[]args) {
    	new VoyageFenetre(null);
    }
}