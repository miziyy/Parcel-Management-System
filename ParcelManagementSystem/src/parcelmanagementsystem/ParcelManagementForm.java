package parcelmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

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
        setLocationRelativeTo(null);

        DatabaseConnection.createTable();

        createGUI();

        loadTable();
    }

    private void createGUI() {
        //Border Layout
        setLayout(new BorderLayout());


        JLabel lblTitle = new JLabel(
                "PARCEL MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        add(lblTitle, BorderLayout.NORTH);

        //Input panel
        JPanel inputPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

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

        inputPanel.add(new JLabel("Tracking Number:"));
        inputPanel.add(txtTracking);

        inputPanel.add(new JLabel("Sender Name:"));
        inputPanel.add(txtSender);

        inputPanel.add(new JLabel("Receiver Name:"));
        inputPanel.add(txtReceiver);

        inputPanel.add(new JLabel("Weight (KG):"));
        inputPanel.add(txtWeight);

        inputPanel.add(new JLabel("Parcel Type:"));
        inputPanel.add(cmbType);

        inputPanel.add(new JLabel("Parcel Status:"));
        inputPanel.add(cmbStatus);


        //Button panel
        JPanel buttonPanel = new JPanel();

        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);


        String[] columns = {
            "Tracking No",
            "Sender",
            "Receiver",
            "Weight",
            "Type",
            "Status",
            "Delivery Fee"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

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

        String type =
                cmbType.getSelectedItem().toString();

        String status =
                cmbStatus.getSelectedItem().toString();

        // VALIDATION
        if (tracking.isEmpty()
                || sender.isEmpty()
                || receiver.isEmpty()
                || weightText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        double weight;

        try {

            weight = Double.parseDouble(weightText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Weight must be a number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        if (weight <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Weight must be greater than 0 KG.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        // POLYMORPHISM
        Parcel parcel;

        if (type.equals("Express")) {

            parcel = new ExpressParcel(
                    tracking,
                    sender,
                    receiver,
                    weight,
                    status
            );

        } else {

            parcel = new StandardParcel(
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

        try (Connection conn =
                     DatabaseConnection.connect();

             PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    parcel.getTrackingNo()
            );

            pstmt.setString(
                    2,
                    parcel.getSenderName()
            );

            pstmt.setString(
                    3,
                    parcel.getReceiverName()
            );

            pstmt.setDouble(
                    4,
                    parcel.getWeight()
            );

            pstmt.setString(
                    5,
                    parcel.getParcelType()
            );

            pstmt.setString(
                    6,
                    parcel.getStatus()
            );

            // Polymorphic method
            pstmt.setDouble(
                    7,
                    parcel.calculateDeliveryFee()
            );

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Parcel saved successfully!\n"
                    + "Delivery Fee: RM "
                    + String.format(
                            "%.2f",
                            parcel.calculateDeliveryFee()
                    )
            );

            loadTable();

            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save parcel.\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
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

        try (Connection conn =
                     DatabaseConnection.connect();

             Statement stmt =
                     conn.createStatement();

             ResultSet rs =
                     stmt.executeQuery(sql)) {

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

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load parcel records.\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Update parcel
    private void updateParcel() {

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

        try (Connection conn =
                     DatabaseConnection.connect();

             PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    parcel.getSenderName()
            );

            pstmt.setString(
                    2,
                    parcel.getReceiverName()
            );

            pstmt.setDouble(
                    3,
                    parcel.getWeight()
            );

            pstmt.setString(
                    4,
                    parcel.getParcelType()
            );

            pstmt.setString(
                    5,
                    parcel.getStatus()
            );

            pstmt.setDouble(
                    6,
                    parcel.calculateDeliveryFee()
            );

            pstmt.setString(
                    7,
                    parcel.getTrackingNo()
            );

            int result = pstmt.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Parcel updated successfully!"
                );

                loadTable();

                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Parcel not found."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update parcel.\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //delete parcel
    private void deleteParcel() {

        String tracking =
                txtTracking.getText().trim();

        if (tracking.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a parcel first."
            );

            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete parcel "
                        + tracking
                        + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = """
                DELETE FROM parcel
                WHERE tracking_no = ?
                """;

        try (Connection conn =
                     DatabaseConnection.connect();

             PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    tracking
            );

            int result =
                    pstmt.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Parcel deleted successfully!"
                );

                loadTable();

                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Parcel not found."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete parcel.\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //display selected row
    private void displaySelectedRow() {

        int row =
                table.getSelectedRow();

        if (row == -1) {
            return;
        }

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

        cmbType.setSelectedItem(
                model.getValueAt(row, 4).toString()
        );

        cmbStatus.setSelectedItem(
                model.getValueAt(row, 5).toString()
        );

        txtTracking.setEditable(false);
    }

//Clear
    private void clearFields() {

        txtTracking.setText("");
        txtSender.setText("");
        txtReceiver.setText("");
        txtWeight.setText("");

        cmbType.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        txtTracking.setEditable(true);

        table.clearSelection();
    }
}