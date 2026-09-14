package parcelmanagementsystem;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ParcelManagementForm extends JFrame {
    
    // text fields used to receive parcel information
    private JTextField txtTracking;
    private JTextField txtSender;
    private JTextField txtReceiver;
    private JTextField txtWeight;
    
    //allow user to choose parcel type
    private JComboBox<String> cmbType;
    private JComboBox<String> cmbStatus;
    
    //button for CRUD operation
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    
    //display all table from database
    private JTable table;
    
    //control data inside JTable
    private DefaultTableModel model;

    public ParcelManagementForm() {
        
        
        //Title
        setTitle("Parcel Management System");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        DatabaseConnection.createTable();

        createGUI();
        loadTable();
        
        setLocationRelativeTo(null);
    }

     private void createGUI() {

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        setContentPane(mainPanel);

        // System title
        JLabel lblTitle = new JLabel(
                "PARCEL MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        //Input panel with title
        JPanel inputPanel = new JPanel(
        new GridLayout(3, 2, 10, 10)
        );
        inputPanel.setBorder(
        BorderFactory.createTitledBorder(" Parcel Details ")
        );

        txtTracking = new JTextField();
        txtSender = new JTextField();
        txtReceiver = new JTextField();
        txtWeight = new JTextField();

        cmbType = new JComboBox<>(
                new String[]{"Standard", "Express"}
        );

        cmbStatus = new JComboBox<>(
                new String[]{
                    "Pending",
                    "Received",
                    "In Transit",
                    "Out for Delivery",
                    "Delivered"
                }
        );

        // Row 1
        inputPanel.add(createInputRow("Tracking Number:", txtTracking));
        inputPanel.add(createInputRow("Weight (KG):", txtWeight));
        
        // Row 2
        inputPanel.add(createInputRow("Sender Name:", txtSender));
        inputPanel.add(createInputRow("Parcel Type:", cmbType));
        
        // Row 3
        inputPanel.add(createInputRow("Receiver Name:", txtReceiver));
        inputPanel.add(createInputRow("Parcel Status:", cmbStatus));


        //Button panel
        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        
        // Colour button
        java.awt.Color green = new java.awt.Color(46, 125, 50);
        java.awt.Color blue = new java.awt.Color(21, 101, 192);
        java.awt.Color red = new java.awt.Color(198, 40, 40);
        java.awt.Color gray = new java.awt.Color(117, 117, 117);
        
        java.util.function.BiConsumer<JButton, java.awt.Color> styleButton = (btn, bg) -> {
            btn.setPreferredSize(new java.awt.Dimension(100, 32));
            btn.setBackground(bg);
            btn.setForeground(java.awt.Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        };
        
        // Style each button
        styleButton.accept(btnSave, green);
        styleButton.accept(btnUpdate, blue);
        styleButton.accept(btnDelete, red);
        styleButton.accept(btnClear, gray);
                
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout(10,15));

        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);


        // Search panel
        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new java.awt.Dimension(180, 26));
        
        JButton btnSearch = new JButton("Search");
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        //Real time filter
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = txtSearch.getText().trim().toLowerCase();
                javax.swing.table.TableRowSorter<DefaultTableModel> sorter = 
                        new javax.swing.table.TableRowSorter<>(model);
                table.setRowSorter(sorter);
                sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(query))
                );
            }
        });
        
        
        // Table
        String[] columns = {
            "Tracking No",
            "Sender Name",
            "Receiver Name",
            "Weight (KG)",
            "Type",
            "Status",
            "Delivery Fee"
        };

        model = new DefaultTableModel(columns, 0){
        
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        
        // Align columns
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Tracking No
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Weight
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Type
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Status
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);  // Fee

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // The form keeps its required height.
        // The table uses the remaining window space.
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        //button events
        btnSave.addActionListener(e -> saveParcel());

        btnUpdate.addActionListener(e -> updateParcel());

        btnDelete.addActionListener(e -> deleteParcel());

        btnClear.addActionListener(e -> clearFields());

        //table click event
        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                displaySelectedRow();
            }
        });
    }

    //create object
    private Parcel createParcelObject() {

        String tracking = txtTracking.getText().trim();
        String sender = txtSender.getText().trim();
        String receiver = txtReceiver.getText().trim();
        String weightText = txtWeight.getText().trim();

        String type = (String) cmbType.getSelectedItem();
        String status = (String) cmbStatus.getSelectedItem();


        // VALIDATION
        if (tracking.isEmpty()
                || sender.isEmpty()
                || receiver.isEmpty()
                || weightText.isEmpty()) {

            showValidationMessage("Please complete all fields.");
            return null;
        }

        double weight;

        try {

            weight = Double.parseDouble(weightText);

        } catch (NumberFormatException e) {

            showValidationMessage("Weight must be a number.");
            return null;
        }

        if (weight <= 0) {

            showValidationMessage("Weight must be a finite number greater than 0 KG."
            );
            return null;
        }

        // POLYMORPHISM
        Parcel parcel;

        if ("Express".equals(type)) {

            parcel = new ExpressParcel(
                    tracking,
                    sender,
                    receiver,
                    weight,
                    status
            );

        } else {

            parcel = new NormalParcel(
                    tracking,
                    sender,
                    receiver,
                    weight,
                    status
            );
        }

        return parcel;
    }

     // SAVE / CREATE
    private void saveParcel() {

        Parcel parcel = createParcelObject();

        if (parcel == null) {
            return;
        }

        String sql = """
                INSERT INTO parcel
                (tracking_no,
                 sender_name,
                 receiver_name,
                 weight,
                 parcel_type,
                 status,
                 delivery_fee)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
            
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parcel.getTrackingNo());
            pstmt.setString(2, parcel.getSenderName());
            pstmt.setString(3, parcel.getReceiverName());
            pstmt.setDouble(4, parcel.getWeight());
            pstmt.setString(5, parcel.getParcelType());
            pstmt.setString(6, parcel.getStatus());
            pstmt.setDouble(7, parcel.calculateDeliveryFee());

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(
                   this,
                    "Parcel saved successfully!"
            );

            loadTable();
            clearFields();

        } catch (SQLException e) {

            showDatabaseError("Unable to save parcel.", e);
        }
    }

    // READ / DISPLAY
    private void loadTable() {

        model.setRowCount(0);

        String sql = """
                SELECT *
                FROM parcel
                ORDER BY tracking_no
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Remove previous rows before loading records
            model.setRowCount(0);

            while (rs.next()) {

                Object[] row = {

                    rs.getString("tracking_no"),

                    rs.getString("sender_name"),

                    rs.getString("receiver_name"),

                    rs.getDouble("weight"),

                    rs.getString("parcel_type"),

                    rs.getString("status"),

                    String.format(
                            "RM %.2f",
                            rs.getDouble("delivery_fee")
                    )
                };

                model.addRow(row);
            }

        } catch (SQLException e) {

            showDatabaseError("Unable to load parcel records.", e);
        }
    }

    // Update parcel
    private void updateParcel() {
        
        if (table.getSelectedRow() == -1) {

            showValidationMessage(
                    "Please select a parcel from the table first."
            );

            return;
        }

        Parcel parcel = createParcelObject();

        if (parcel == null) {
            return;
        }

        String sql = """
                UPDATE parcel
                SET sender_name = ?,
                    receiver_name = ?,
                    weight = ?,
                    parcel_type = ?,
                    status = ?,
                    delivery_fee = ?
                WHERE tracking_no = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parcel.getSenderName());
            pstmt.setString(2, parcel.getReceiverName());
            pstmt.setDouble(3, parcel.getWeight());
            pstmt.setString(4, parcel.getParcelType());
            pstmt.setString(5, parcel.getStatus());
            pstmt.setDouble(6, parcel.calculateDeliveryFee());
            pstmt.setString(7, parcel.getTrackingNo());

            int result = pstmt.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Parcel updated successfully!"
                );

                loadTable();

                clearFields();

            } else {
                
                showValidationMessage("Parcel record not found.");

            }

        } catch (SQLException e) {

            showDatabaseError("Unable to update parcel.", e);
        }
    }

    //delete parcel
    private void deleteParcel() {

        if (table.getSelectedRow() == -1) {

            showValidationMessage(
                    "Please select a parcel from the table first."
            );

            return;
        }

        String tracking = txtTracking.getText().trim();


        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete parcel " + tracking + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = """
                DELETE FROM parcel
                WHERE tracking_no = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tracking);

            int result = pstmt.executeUpdate();


            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Parcel deleted successfully!"
                );

                loadTable();

                clearFields();

            } else {

                showValidationMessage("Parcel record not found.");
            }

        } catch (SQLException e) {

            showDatabaseError("Unable to delete parcel.", e);
        }
    }

    //display selected row
    private void displaySelectedRow() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }
        
        int row = table.convertRowIndexToModel(selectedRow);
        
        txtTracking.setText(
                model.getValueAt(row, 0).toString()
        );

        txtSender.setText(
                model.getValueAt(row, 1).toString()
        );

        txtReceiver.setText(
                model.getValueAt(row, 2).toString()
        );

        txtWeight.setText(
                model.getValueAt(row, 3).toString()
        );

        String type = model.getValueAt(row, 4).toString();

        // Accept either Normal or Standard for non-express parcels
        cmbType.setSelectedItem(
                "Express".equalsIgnoreCase(type)
                        ? "Express"
                        : "Standard"
        );

        cmbStatus.setSelectedItem(
                model.getValueAt(row, 5).toString()
        );

        // Keep the primary key unchanged during an update
        txtTracking.setEditable(false);
        btnSave.setEnabled(false);
    }

//Clear
    private void clearFields() {
        table.clearSelection();

        txtTracking.setText("");
        txtSender.setText("");
        txtReceiver.setText("");
        txtWeight.setText("");

        cmbType.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        txtTracking.setEditable(true);
        btnSave.setEnabled(true);

        txtTracking.requestFocusInWindow();
    }
    private void showValidationMessage(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showDatabaseError(
            String message,
            SQLException exception
    ) {

        JOptionPane.showMessageDialog(
                this,
                message + "\n" + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    
    // Located input fields after their labels
    private JPanel createInputRow(String labelText, JComponent inputComponent) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        
        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        label.setPreferredSize(new java.awt.Dimension(130, 25));
        
        inputComponent.setPreferredSize(new java.awt.Dimension(220, 25));
        
        rowPanel.add(label);
        rowPanel.add(inputComponent);
        
        return rowPanel;
    }
    
}
    
    
