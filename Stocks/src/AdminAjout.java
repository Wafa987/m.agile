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
    private Image backgroundImage;  // Background image
    private JPanel navBar;

    public class DateLabelFormatter extends AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    public AdminAjout(Voyageur voyageur) {
        this.voyageur = voyageur;
        voyageDAO = new VoyageDAO();

        // Load background image
        try {
            backgroundImage = new ImageIcon("../voyage.jpg").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image de fond non trouvée");
        }


        setTitle("Gestion des Voyages");
        setSize(1800, 1600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background panel
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

        // Initialize navigation bar (like VoyageFenetre & PageAcceuilFenetre)
        navBar = createNavBar();

        // Create content panel (like in VoyageFenetre)
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

    // Method to create the navigation bar
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
            @Override
            public void actionPerformed(ActionEvent e) {
                new PageAcceuilFenetre();  // Open PageAcceuilFenetre
                dispose();              // Close AdminAjout
            }
        });

        // Add the logout button to the navigation bar
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

    // Classe pour le panel de boutons
    class ButtonsPanel extends JPanel {
        private final JButton modifierBtn;
        private final JButton supprimerBtn;
        private int row;
        private JTable table;

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

        public void setRow(int row) {
            this.row = row;
        }

        public JButton getModifierBtn() {
            return modifierBtn;
        }

        public JButton getSupprimerBtn() {
            return supprimerBtn;
        }
    }

    // Classe pour le renderer des boutons
    class ButtonsRenderer extends JPanel implements TableCellRenderer {
        private final ButtonsPanel panel;

        public ButtonsRenderer(JTable table) {
            panel = new ButtonsPanel(table);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setRow(row);
            return panel;
        }
    }

    // Classe pour l'editor des boutons
    class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {
        private final ButtonsPanel panel;
        private final JTable table;
        private AdminAjout adminAjoutInstance;

        public ButtonsEditor(final JTable table, AdminAjout admin) {
             this.adminAjoutInstance = admin;
            this.table = table;
            panel = new ButtonsPanel(table);

            panel.getModifierBtn().addActionListener(new ActionListener() {
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

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            panel.setRow(row);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

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

    // Correct place for `createButton` method: inside the `VoyageFenetre` class
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

     private JButton createNavButton(String text) {
        final JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 30)); // smaller height navigation Button
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // smaller margin
        button.setFont(new Font("Arial", Font.BOLD, 14)); // smaller police

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


    public static void main(String[] args) {
        new AdminAjout(null);
    }

}