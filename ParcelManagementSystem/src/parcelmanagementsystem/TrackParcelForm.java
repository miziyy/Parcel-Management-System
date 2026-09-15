package parcelmanagementsystem;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class TrackParcelForm extends JFrame {

    // Text field for user to enter tracking number
    private JTextField txtTrackingNo;

    // Labels to show parcel information
    private JLabel lblTrackingNo;
    private JLabel lblSender;
    private JLabel lblReceiver;
    private JLabel lblWeight;
    private JLabel lblStatus;
    private JLabel lblParcelType;
    private JLabel lblDeliveryFee;

    // Buttons
    private JButton btnSearch;
    private JButton btnClear;
    private JButton btnBack;

    public TrackParcelForm() {

        // Window setting
        setTitle("Track Parcel");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createForm();
    }

    private void createForm() {

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(204, 229, 255));

        // Title
        JLabel lblTitle = new JLabel("TRACK PARCEL");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(150, 30, 350, 40);

        panel.add(lblTitle);

        // Tracking number label
        JLabel lblSearch = new JLabel("Tracking No:");
        lblSearch.setBounds(70, 100, 120, 30);

        // Tracking number text field
        txtTrackingNo = new JTextField();
        txtTrackingNo.setBounds(180, 100, 250, 30);

        // Search button
        btnSearch = new JButton("Search");
        btnSearch.setBounds(450, 100, 100, 30);

        panel.add(lblSearch);
        panel.add(txtTrackingNo);
        panel.add(btnSearch);

        // Panel to show parcel information
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(null);
        resultPanel.setBackground(Color.WHITE);

        resultPanel.setBorder(
                BorderFactory.createTitledBorder("Parcel Information")
        );

        resultPanel.setBounds(70, 160, 480, 260);

        // Result labels
        lblTrackingNo = new JLabel("Tracking No: -");
        lblTrackingNo.setBounds(30, 30, 400, 25);

        lblSender = new JLabel("Sender: -");
        lblSender.setBounds(30, 60, 400, 25);

        lblReceiver = new JLabel("Receiver: -");
        lblReceiver.setBounds(30, 90, 400, 25);

        lblWeight = new JLabel("Weight: -");
        lblWeight.setBounds(30, 120, 400, 25);

        lblStatus = new JLabel("Status: -");
        lblStatus.setBounds(30, 150, 400, 25);

        lblParcelType = new JLabel("Parcel Type: -");
        lblParcelType.setBounds(30, 180, 400, 25);

        lblDeliveryFee = new JLabel("Delivery Fee: -");
        lblDeliveryFee.setBounds(30, 210, 400, 25);

        // Add result labels into result panel
        resultPanel.add(lblTrackingNo);
        resultPanel.add(lblSender);
        resultPanel.add(lblReceiver);
        resultPanel.add(lblWeight);
        resultPanel.add(lblStatus);
        resultPanel.add(lblParcelType);
        resultPanel.add(lblDeliveryFee);

        panel.add(resultPanel);

        // Clear button
        btnClear = new JButton("Clear");
        btnClear.setBounds(180, 450, 120, 35);

        // Back button
        btnBack = new JButton("Back");
        btnBack.setBounds(320, 450, 120, 35);

        panel.add(btnClear);
        panel.add(btnBack);

        // Search button action
        btnSearch.addActionListener(e -> searchParcel());

        // Clear button action
        btnClear.addActionListener(e -> clearForm());

        // Back button action
        btnBack.addActionListener(e -> {

            new MainMenu().setVisible(true);

            // Close current window
            dispose();
        });

        // Press Enter to search
        txtTrackingNo.addActionListener(e -> searchParcel());

        add(panel);
    }

    private void searchParcel() {

        // Get tracking number entered by user
        String trackingNo = txtTrackingNo.getText().trim();

        // Check if text field is empty
        if (trackingNo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a tracking number."
            );

            return;
        }

        // SQL command to search parcel
        String sql =
                "SELECT * FROM parcel WHERE tracking_no = ?";

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            // Put tracking number into SQL
            pstmt.setString(1, trackingNo);

            // Run SQL command
            ResultSet rs = pstmt.executeQuery();

            // If parcel is found
            if (rs.next()) {

                // Get data from database
                String senderName =
                        rs.getString("sender_name");

                String receiverName =
                        rs.getString("receiver_name");

                double weight =
                        rs.getDouble("weight");

                String status =
                        rs.getString("status");

                String parcelType =
                        rs.getString("parcel_type");

                Parcel parcel;

                // Create ExpressParcel object
                if (parcelType.equalsIgnoreCase("Express")) {

                    parcel = new ExpressParcel(
                            trackingNo,
                            senderName,
                            receiverName,
                            weight,
                            status
                    );

                } else {

                    // Create NormalParcel object
                    parcel = new NormalParcel(
                            trackingNo,
                            senderName,
                            receiverName,
                            weight,
                            status
                    );
                }

                // Show parcel information on screen
                lblTrackingNo.setText(
                        "Tracking No: " + parcel.getTrackingNo()
                );

                lblSender.setText(
                        "Sender: " + parcel.getSenderName()
                );

                lblReceiver.setText(
                        "Receiver: " + parcel.getReceiverName()
                );

                lblWeight.setText(
                        "Weight: " + parcel.getWeight() + " kg"
                );

                lblStatus.setText(
                        "Status: " + parcel.getStatus()
                );

                lblParcelType.setText(
                        "Parcel Type: " + parcel.getParcelType()
                );

                lblDeliveryFee.setText(
                        String.format(
                                "Delivery Fee: RM %.2f",
                                parcel.calculateDeliveryFee()
                        )
                );

            } else {

                // If parcel is not found
                JOptionPane.showMessageDialog(
                        this,
                        "Parcel not found."
                );

                clearResult();
            }

        } catch (Exception e) {

            // Show error if database problem happens
            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    private void clearForm() {

        // Clear tracking number text field
        txtTrackingNo.setText("");

        // Clear parcel information
        clearResult();

        // Move cursor back to tracking number field
        txtTrackingNo.requestFocus();
    }

    private void clearResult() {

        // Reset all labels
        lblTrackingNo.setText("Tracking No: -");
        lblSender.setText("Sender: -");
        lblReceiver.setText("Receiver: -");
        lblWeight.setText("Weight: -");
        lblStatus.setText("Status: -");
        lblParcelType.setText("Parcel Type: -");
        lblDeliveryFee.setText("Delivery Fee: -");
    }
}