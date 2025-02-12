import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Properties;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField.AbstractFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 * Classe AdminAjout qui permet d'afficher et de gérer l'ajout, la modification et la suppression des voyages.
 * Hérite de JFrame et implémente ActionListener pour gérer les événements des boutons.
 */
public class AdminAjout extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField textFieldDepart, textFieldDescription, textFieldDestination;
    private JTextField textFieldPlacesDisponibles, textFieldPrix;
    private JButton boutonAjouter;
    private JTable table;
    private DefaultTableModel tableModel;
    private VoyageDAO voyageDAO;
    private Voyageur voyageur;
    private int selectedVoyageId = -1;
    private JDatePickerImpl datePickerDepart, datePickerRetour;
    private Image backgroundImage;  // Image de fond
    private JPanel navBar;

    /**
     * Classe interne pour formater les dates dans les champs de texte.
     * Gère la conversion entre les objets Date et les chaînes de caractères.
     */
    public class DateLabelFormatter extends AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        /**
         * Convertit une chaîne de caractères en objet Date.
         * @param text La chaîne de caractères à convertir.
         * @return Un objet Date représentant la chaîne, ou null si la conversion échoue.
         * @throws ParseException Si la chaîne ne peut pas être parsée selon le format de date.
         */
        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parse(text);
        }

        /**
         * Convertit un objet Date en chaîne de caractères.
         * @param value L'objet Date à convertir.
         * @return Une chaîne de caractères représentant la date, ou une chaîne vide si la valeur est null.
         * @throws ParseException Si la date ne peut pas être formatée.
         */
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    /**
     * Constructeur de la classe AdminAjout.
     * Initialise l'interface graphique, charge les données et configure les actions des boutons.
     * @param voyageur L'utilisateur connecté (peut être null si non connecté).
     */
    public AdminAjout(Voyageur voyageur) {
        this.voyageur = voyageur;
        voyageDAO = new VoyageDAO();

        // Chargement de l'image de fond
        try {
            backgroundImage = new ImageIcon("resources/voyage.jpg").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image de fond non trouvée");
        }


        setTitle("Gestion des Voyages");
        setSize(1800, 1600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panneau de fond
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
        navBar = createNavBar();

        // Création du panneau de contenu
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(200, 200, 200, 230));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));



        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations du voyage"));

        // Initialisation des champs
        textFieldDepart = new JTextField();
        textFieldDescription = new JTextField();
        textFieldDestination = new JTextField();
        textFieldPlacesDisponibles = new JTextField();
        textFieldPrix = new JTextField();

        // Configuration des JDatePicker
        UtilDateModel modelDepart = new UtilDateModel();
        UtilDateModel modelRetour = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Aujourd'hui");
        p.put("text.month", "Mois");
        p.put("text.year", "Année");

        JDatePanelImpl datePanelDepart = new JDatePanelImpl(modelDepart, p);
        JDatePanelImpl datePanelRetour = new JDatePanelImpl(modelRetour, p);

        datePickerDepart = new JDatePickerImpl(datePanelDepart, new DateLabelFormatter());
        datePickerRetour = new JDatePickerImpl(datePanelRetour, new DateLabelFormatter());

        formPanel.add(new JLabel("Départ :"));
        formPanel.add(textFieldDepart);
        formPanel.add(new JLabel("Description :"));
        formPanel.add(textFieldDescription);
        formPanel.add(new JLabel("Destination :"));
        formPanel.add(textFieldDestination);
        formPanel.add(new JLabel("Date de départ :"));
        formPanel.add(datePickerDepart);
        formPanel.add(new JLabel("Date de retour :"));
        formPanel.add(datePickerRetour);
        formPanel.add(new JLabel("Places disponibles :"));
        formPanel.add(textFieldPlacesDisponibles);
        formPanel.add(new JLabel("Prix :"));
        formPanel.add(textFieldPrix);

        // Le reste du code pour les boutons et la table reste identique
        boutonAjouter = new JButton("Ajouter Voyage");
        boutonAjouter.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(boutonAjouter);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Départ", "Destination", "Date Départ", "Date Retour",
                        "Places", "Prix", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);

        TableColumn actionColumn = table.getColumnModel().getColumn(7);
        actionColumn.setPreferredWidth(150);
        actionColumn.setCellRenderer(new ButtonsRenderer(table));
        actionColumn.setCellEditor(new ButtonsEditor(table, this));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        //contentPanel.add(topPanel, BorderLayout.NORTH);  //Don't need this , the search is in topPanel
        contentPanel.add(scrollPane, BorderLayout.CENTER);


        // Add components to content panel

        contentPanel.add(topPanel, BorderLayout.NORTH); // Place the entire form panel at the top


        //add margin
        backgroundPanel.add(Box.createHorizontalStrut(80), BorderLayout.WEST);
        backgroundPanel.add(Box.createHorizontalStrut(80), BorderLayout.EAST);
        backgroundPanel.add(Box.createVerticalStrut(80), BorderLayout.NORTH);
        backgroundPanel.add(Box.createVerticalStrut(80), BorderLayout.SOUTH);

        // Add content panel to background panel
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        // Ajout de la barre de navigation au panel de fond
        backgroundPanel.add(navBar, BorderLayout.NORTH);


        setContentPane(backgroundPanel);

        chargerVoyages();
        setVisible(true);
    }

    /**
     * Méthode pour créer la barre de navigation.
     * La barre contient un bouton de déconnexion.
     * @return Le JPanel représentant la barre de navigation.
     */
    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        //navBar.setPreferredSize(new Dimension(getWidth(), 60)); // Reduced height REMOVE IT
        navBar.setBackground(new Color(51, 51, 51));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        leftPanel.setOpaque(false);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); // Reduced vertical gap
        rightPanel.setOpaque(false);

        // Deconnexion Button
        JButton btnDeconnexion = createNavButton("Déconnexion");
        btnDeconnexion.setPreferredSize(new Dimension(220, 30)); // Smaller button size

        btnDeconnexion.addActionListener(new ActionListener() {
            /**
             * Action effectuée lors du clic sur le bouton Déconnexion.
             * Ouvre la fenêtre principale et ferme la fenêtre actuelle.
             * @param e L'événement d'action.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                new PageAcceuilFenetre();  // Ouvre PageAcceuilFenetre
                dispose();              // Ferme AdminAjout
            }
        });

        // Ajout du bouton de déconnexion à la barre de navigation
        rightPanel.add(btnDeconnexion);

        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    /*// Updated `updateImageSize` method to account for the navBar REMOVE IT
    private void updateImageSize() {
        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight() - navBar.getHeight(); // Reduced to 60
            Image scaledImage = backgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
    }*/

    /**
     * Classe interne pour le panneau des boutons Modifier et Supprimer dans la table.
     */
    class ButtonsPanel extends JPanel {
        private final JButton modifierBtn;
        private final JButton supprimerBtn;
        private int row;
        private JTable table;

        /**
         * Constructeur du panneau des boutons.
         * @param table La JTable à laquelle les boutons sont associés.
         */
        public ButtonsPanel(JTable table) {
            this.table = table;
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

            modifierBtn = new JButton("Modifier");
            supprimerBtn = new JButton("Supprimer");

            modifierBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            supprimerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            add(modifierBtn);
            add(supprimerBtn);
        }

        /**
         * Définit la ligne à laquelle le panneau est associé.
         * @param row L'index de la ligne.
         */
        public void setRow(int row) {
            this.row = row;
        }

        /**
         * Obtient le bouton Modifier.
         * @return Le bouton Modifier.
         */
        public JButton getModifierBtn() {
            return modifierBtn;
        }

        /**
         * Obtient le bouton Supprimer.
         * @return Le bouton Supprimer.
         */
        public JButton getSupprimerBtn() {
            return supprimerBtn;
        }
    }

    /**
     * Classe interne pour le rendu des boutons Modifier et Supprimer dans la table.
     */
    class ButtonsRenderer extends JPanel implements TableCellRenderer {
        private final ButtonsPanel panel;

        /**
         * Constructeur du rendu des boutons.
         * @param table La JTable pour laquelle les boutons sont rendus.
         */
        public ButtonsRenderer(JTable table) {
            panel = new ButtonsPanel(table);
        }

        /**
         * Retourne le composant utilisé pour rendre la cellule.
         * @param table La JTable qui rend la cellule.
         * @param value La valeur à rendre.
         * @param isSelected Indique si la cellule est sélectionnée.
         * @param hasFocus Indique si la cellule a le focus.
         * @param row L'index de la ligne de la cellule.
         * @param column L'index de la colonne de la cellule.
         * @return Le composant utilisé pour rendre la cellule.
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setRow(row);
            return panel;
        }
    }

    /**
     * Classe interne pour l'éditeur des boutons Modifier et Supprimer dans la table.
     * Permet de gérer les actions associées aux boutons.
     */
    class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {
        private final ButtonsPanel panel;
        private final JTable table;
        private AdminAjout adminAjoutInstance;

        /**
         * Constructeur de l'éditeur des boutons.
         * @param table La JTable pour laquelle les boutons sont édités.
         * @param admin L'instance de AdminAjout.
         */
        public ButtonsEditor(final JTable table, AdminAjout admin) {
             this.adminAjoutInstance = admin;
            this.table = table;
            panel = new ButtonsPanel(table);

            panel.getModifierBtn().addActionListener(new ActionListener() {
                /**
                 * Action effectuée lors du clic sur le bouton Modifier.
                 * Charge les informations du voyage sélectionné dans les champs de texte pour modification.
                 * @param e L'événement d'action.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {

                    int modelRow = table.convertRowIndexToModel(panel.row);
                    selectedVoyageId = (int) table.getModel().getValueAt(modelRow, 0);
                    System.out.println("modif " + (int) table.getModel().getValueAt(modelRow, 0));
                    int id = (int) table.getModel().getValueAt(modelRow, 0);
                    Voyage v = voyageDAO.getVoyage(id);
                    textFieldDepart.setText(v.getDepart());
                    textFieldDescription.setText(v.getDescription());
                    textFieldDestination.setText(v.getDestination());

                    // Set dates in datePickers
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateDepart = dateFormat.parse(v.getDateDepart());
                        Date dateRetour = dateFormat.parse(v.getDateRetour());

                        Calendar calDepart = Calendar.getInstance();
                        Calendar calRetour = Calendar.getInstance();
                        calDepart.setTime(dateDepart);
                        calRetour.setTime(dateRetour);

                        datePickerDepart.getModel().setDate(
                            calDepart.get(Calendar.YEAR),
                            calDepart.get(Calendar.MONTH),
                            calDepart.get(Calendar.DAY_OF_MONTH)
                        );
                        datePickerDepart.getModel().setSelected(true); // Ensure date is marked as selected

                        datePickerRetour.getModel().setDate(
                            calRetour.get(Calendar.YEAR),
                            calRetour.get(Calendar.MONTH),
                            calRetour.get(Calendar.DAY_OF_MONTH)
                        );
                        datePickerRetour.getModel().setSelected(true); // Ensure date is marked as selected

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    textFieldPlacesDisponibles.setText(String.valueOf(v.getPlacesDisponibles()));
                    textFieldPrix.setText(String.valueOf(v.getPrix()));
                    fireEditingStopped();
                    boutonAjouter.setText("Modifier");
                }
            });

            panel.getSupprimerBtn().addActionListener(new ActionListener() {
                /**
                 * Action effectuée lors du clic sur le bouton Supprimer.
                 * Affiche une confirmation avant de supprimer le voyage sélectionné.
                 * @param e L'événement d'action.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    int modelRow = table.convertRowIndexToModel(panel.row);
                    int id = (int) table.getModel().getValueAt(modelRow, 0);
                    // Afficher la confirmation avant de supprimer
                    int confirmation = JOptionPane.showConfirmDialog(AdminAjout.this,
                            "Êtes-vous sûr de vouloir supprimer ce voyage ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        voyageDAO.supprimer(id);
                        fireEditingStopped();
                        chargerVoyages();
                    }
                }
            });
        }

        /**
         * Retourne le composant éditeur de cellule.
         * @param table La JTable en cours d'édition.
         * @param value La valeur de la cellule en cours d'édition.
         * @param isSelected Indique si la cellule est sélectionnée.
         * @param row L'index de la ligne.
         * @param column L'index de la colonne.
         * @return Le composant utilisé pour éditer la cellule.
         */
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            panel.setRow(row);
            return panel;
        }

        /**
         * Retourne la valeur de la cellule après édition.
         * @return Une chaîne vide.
         */
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    /**
     * Charge les voyages depuis la base de données et met à jour le modèle de la table.
     */
    private void chargerVoyages() {
        tableModel.setRowCount(0);
        List<Voyage> voyages = voyageDAO.getListeVoyages();
        for (Voyage v : voyages) {
            tableModel.addRow(new Object[]{
                v.getId(),
                v.getDepart(),
                v.getDestination(),
                v.getDateDepart(),
                v.getDateRetour(),
                v.getPlacesDisponibles(),
                v.getPrix(),
                ""  // Pour la colonne d'actions
            });
        }
    }

    /**
     * Charge les informations d'un voyage dans les champs de texte pour modification.
     * @param id L'ID du voyage à charger.
     */
    public void chargerVoyagePourModification(int id) {
        Voyage voyage = voyageDAO.getVoyage(id);
        selectedVoyageId = id;

        if (voyage != null) {
            textFieldDepart.setText(voyage.getDepart());
            textFieldDescription.setText(voyage.getDescription());
            textFieldDestination.setText(voyage.getDestination());

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateDepart = dateFormat.parse(voyage.getDateDepart());
                Date dateRetour = dateFormat.parse(voyage.getDateRetour());

                Calendar calDepart = Calendar.getInstance();
                Calendar calRetour = Calendar.getInstance();
                calDepart.setTime(dateDepart);
                calRetour.setTime(dateRetour);

                datePickerDepart.getModel().setDate(
                    calDepart.get(Calendar.YEAR),
                    calDepart.get(Calendar.MONTH),
                    calDepart.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDepart.getModel().setSelected(true); // Important to show the date in the picker

                datePickerRetour.getModel().setDate(
                    calRetour.get(Calendar.YEAR),
                    calRetour.get(Calendar.MONTH),
                    calRetour.get(Calendar.DAY_OF_MONTH)
                );
                datePickerRetour.getModel().setSelected(true); // Important to show the date in the picker

            } catch (ParseException e) {
                e.printStackTrace();
            }

            textFieldPlacesDisponibles.setText(String.valueOf(voyage.getPlacesDisponibles()));
            textFieldPrix.setText(String.valueOf(voyage.getPrix()));
            boutonAjouter.setText("Modifier Voyage");
        }
    }



    /**
     * Gère les actions des boutons de la fenêtre.
     * @param e L'événement d'action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonAjouter) {
            if (boutonAjouter.getText().equals("Modifier")) {
                modifierVoyage();
            } else {
                ajouterVoyage();
            }
        }
    }

    /**
     * Ajoute un nouveau voyage dans la base de données.
     * Récupère les informations du voyage depuis les champs de texte et les JDatePickers.
     */
    private void ajouterVoyage() {
        try {
            int placesDisponibles = Integer.parseInt(textFieldPlacesDisponibles.getText());
            double prix = Double.parseDouble(textFieldPrix.getText());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateDepart = dateFormat.format(datePickerDepart.getModel().getValue());
            String dateRetour = dateFormat.format(datePickerRetour.getModel().getValue());

            Voyage voyage = new Voyage(0,
                textFieldDepart.getText(),
                textFieldDescription.getText(),
                textFieldDestination.getText(),
                dateDepart,
                dateRetour,
                placesDisponibles,
                prix);

            if (voyageDAO.ajouter(voyage) > 0) {
                JOptionPane.showMessageDialog(this, "Voyage ajouté avec succès!");
                viderChamps();
                chargerVoyages();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du voyage.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides pour les places et le prix.");
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez choisir une date de départ et de retour.");
        }
    }

    /**
     * Modifie un voyage existant dans la base de données.
     * Récupère les informations du voyage depuis les champs de texte et les JDatePickers.
     */
    private void modifierVoyage() {
        try {
            int placesDisponibles = Integer.parseInt(textFieldPlacesDisponibles.getText());
            double prix = Double.parseDouble(textFieldPrix.getText());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateDepart = dateFormat.format(datePickerDepart.getModel().getValue());
            String dateRetour = dateFormat.format(datePickerRetour.getModel().getValue());

            Voyage voyage = new Voyage(selectedVoyageId,
                textFieldDepart.getText(),
                textFieldDescription.getText(),
                textFieldDestination.getText(),
                dateDepart,
                dateRetour,
                placesDisponibles,
                prix);

            if (voyageDAO.mettreAJour(voyage) > 0) {
                JOptionPane.showMessageDialog(this, "Voyage modifié avec succès!");
                viderChamps();
                selectedVoyageId = -1;
                boutonAjouter.setText("Ajouter Voyage");
                chargerVoyages();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du voyage.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez choisir une date de départ et de retour.");
        }
    }

    /**
     * Vide les champs de texte et les JDatePickers.
     */
    private void viderChamps() {
        textFieldDepart.setText("");
        textFieldDescription.setText("");
        textFieldDestination.setText("");
        datePickerDepart.getModel().setValue(null);
        datePickerDepart.getModel().setSelected(false); // Also deselect the date in the picker
        datePickerRetour.getModel().setValue(null);
        datePickerRetour.getModel().setSelected(false);
        textFieldPlacesDisponibles.setText("");
        textFieldPrix.setText("");
    }

    /**
     * Crée un bouton avec un style spécifique.
     * @param button Le bouton à styliser.
     * @param color La couleur de fond du bouton.
     * @return Le bouton stylisé.
     */
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
     * Crée un JButton avec un style spécifique pour la barre de navigation.
     * @param text Le texte à afficher sur le bouton.
     * @return Le JButton avec le style appliqué.
     */
     private JButton createNavButton(String text) {
        final JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 30)); // smaller height navigation Button
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // smaller margin
        button.setFont(new Font("Arial", Font.BOLD, 14)); // smaller police

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Action effectuée lorsque la souris entre dans la zone du bouton.
             * Change la couleur de fond du bouton.
             * @param evt L'événement de souris.
             */
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }

            /**
             * Action effectuée lorsque la souris sort de la zone du bouton.
             * Rétablit la couleur de fond du bouton.
             * @param evt L'événement de souris.
             */
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }


    /**
     * Méthode principale qui lance l'application en créant une instance de AdminAjout.
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        new AdminAjout(null);
    }

}