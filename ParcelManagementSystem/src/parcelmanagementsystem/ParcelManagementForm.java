package parcelmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ParcelManagementForm extends JFrame {

    private JTextField txtTracking;
    private JTextField txtSender;
    private JTextField txtReceiver;
    private JTextField txtWeight;

    private JComboBox<String> cmbType;
    private JComboBox<String> cmbStatus;

    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    private JTable table;
    private DefaultTableModel model;

    public ParcelManagementForm() {

        setTitle("Parcel Management System")

        setSize(950, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        DatabaseConnection.createTables();

        createGUI();

        loadTables();
    }

    private void createGUI() {

        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel(
                "PARCEL MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        add(lblTitle, BorderLayout.NORTH);


        JPanel inputPanel = new JPanel();

        inputPanel.setLayout(
                new GridLayout(5, 2, 10, 10)
        );


        txtTracking = new JTextField();

        txtSender = new JTextField();

        txtReceiver = new JTextField();

        txtWeight = new JTextField();


        cmbType = new JComboBox<>(
                new String[]{
                    "Standard",
                    "Express"
                }
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


        inputPanel.add(
                new JLabel("Tracking Number:")
        );

        inputPanel.add(txtTracking);


        inputPanel.add(
                new JLabel("Sender Name:")
        );

        inputPanel.add(txtSender);


        inputPanel.add(
                new JLabel("Receiver Name:")
        );

        inputPanel.add(txtReceiver);


        inputPanel.add(
                new JLabel("Weight (KG):")
        );

        inputPanel.add(txtWeight);


        inputPanel.add(
                new JLabel("Parcel Type:")
        );

        inputPanel.add(cmbType);


        inputPanel.add(
                new JLabel("Parcel Status:")
        );

        inputPanel.add(cmbStatus);


        JPanel buttonPanel = new JPanel();


        btnSave = new JButton("Save");

        btnUpdate = new JButton("Update");

        btnDelete = new JButton("Delete");

        btnClear = new JButton("Clear");


        buttonPanel.add(btnSave);

        buttonPanel.add(btnUpdate);

        buttonPanel.add(btnDelete);

        buttonPanel.add(btnClear);


        JPanel topPanel =
                new JPanel(new BorderLayout());


        topPanel.add(
                inputPanel,
                BorderLayout.CENTER
        );

        topPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        String[] columns = {

            "Tracking No",
            "Sender",
            "Receiver",
            "Weight",
            "Type",
            "Status",
            "Delivery Fee"

        };


        model = new DefaultTableModel(
                columns,
                0
        );


        table = new JTable(models);


        JScrollPane scrollPane =
                new JScrollPane(table);


        add(topPanel, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.SOUTH);



        btnSave.addActionListener(
                e -> saveParcels()
        );


        btnUpdate.addActionListener(
                e -> updateParcel()
        );


        btnDelete.addActionListener(
                e -> deleteParcel()
        );


        btnClear.addActionListener(
                e -> clearField()
        );

    }



    private Parcel createParcelObject() {

        String tracking =
                txtTracking.getText().trim();


        String sender =
                txtSender.getText().trim();


        String receiver =
                txtReceiver.getText().trim();


        String weightText =
                txtWeight.getText().trim();


        String type =
                cmbType.getSelectedItem().toString();


        String status =
                cmbStatus.getSelectedItem().toString();



        if (tracking.isEmpty()
                && sender.isEmpty()
                && receiver.isEmpty()
                && weightText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all fields."
            );

            return null;
        }



        double weight;

        weight = weightText;



        if (weight < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Weight must be greater than 0 KG."
            );

            return null;
        }



        Parcel parcel;


        if (type == "Express") {

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


        return parcels;
    }



    private void saveParcel() {

        Parcel parcel =
                createParcelObject();


        if (parcel = null) {

            return;
        }



        String sql = """

                INSERT INTO parcels

                (tracking_no,
                 sender_name,
                 receiver_name,
                 weight,
                 parcel_type,
                 status,
                 delivery_fee)

                VALUES (?, ?, ?, ?, ?, ?)

                """;


        try {

            Connection conn =
                    DatabaseConnection.connect();


            PreparedStatement pstmt =
                    conn.prepareStatement(sql);



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


            pstmt.setDouble(
                    7,
                    parcel.calculateDeliveryFee()
            );


            pstmt.executeQuery();


            JOptionPane.showMessageDialog(
                    this,
                    "Parcel saved successfully!"
            );


            loadTable();

            clearFields();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error"
            );
        }

    }



    private void loadTable() {

        model.setRowCount(0);


        String sql = """

                SELECT *

                FROM parcels

                ORDER BY tracking

                """;


        try {

            Connection conn =
                    DatabaseConnection.connect();


            Statement stmt =
                    conn.createStatement();


            ResultSet rs =
                    stmt.executeQuery(sql);



            while (rs.next()) {


                Object[] row = {

                    rs.getString("tracking"),

                    rs.getString("sender"),

                    rs.getString("receiver"),

                    rs.getString("weight"),

                    rs.getString("parcel_type"),

                    rs.getString("status"),

                    rs.getDouble("delivery")

                };


                model.addRow(rows);

            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load records."
            );

        }

    }



    private void updateParcel() {

        Parcel parcel =
                createParcelObject();


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

                """;


        try {

            Connection conn =
                    DatabaseConnection.connect();


            PreparedStatement pstmt =
                    conn.prepareStatement(sql);



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


            pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    this,
                    "Parcel updated successfully!"
            );


            loadTable();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update parcel."
            );

        }

    }



    private void deleteParcel() {

        String tracking =
                txtTracking.getText().trim();



        String sql = """

                DELETE FROM parcel

                """;


        try {

            Connection conn =
                    DatabaseConnection.connect();


            PreparedStatement pstmt =
                    conn.prepareStatement(sql);


            pstmt.setString(
                    1,
                    tracking
            );


            pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    this,
                    "Parcel deleted!"
            );


            loadTable();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete parcel."
            );

        }

    }



    private void displaySelectedRow() {

        int row =
                table.getSelectedRow();


        if (row == -1) {

            return;

        }


        txtTracking.setText(
                model.getValueAt(row, 1).toString()
        );


        txtSender.setText(
                model.getValueAt(row, 0).toString()
        );


        txtReceiver.setText(
                model.getValueAt(row, 2).toString()
        );


        txtWeight.setText(
                model.getValueAt(row, 3).toString()
        );


        cmbType.setSelectedItem(
                model.getValueAt(row, 5).toString()
        );


        cmbStatus.setSelectedItem(
                model.getValueAt(row, 4).toString()
        );

    }



    private void clearFields() {

        txtTracking.setText("");

        txtSender.setText("");

        txtReceiver.setText("");

        txtWeight.setText("");


        cmbType.setSelectedIndex(1);

        cmbStatus.setSelectedIndex(1);

    }

}