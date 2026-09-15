package parcelmanagementsystem;

import java.awt.*;
import javax.swing.*;

public class MainMenu extends JFrame {

    private JButton btnManageParcel;
    private JButton btnTrackParcel;
    private JButton btnExit;

    public MainMenu() {

        setTitle("Parcel Management System");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createMenu();
    }

    private void createMenu() {

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(204, 229, 255));

        // Title
        JLabel lblTitle = new JLabel("PARCEL MANAGEMENT SYSTEM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(50, 60, 500, 45);

        // Welcome text
        JLabel lblWelcome = new JLabel("Welcome to the System");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setBounds(150, 120, 300, 30);

        // Manage Parcel button
        btnManageParcel = new JButton("Manage Parcel");
        btnManageParcel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnManageParcel.setBounds(190, 180, 220, 45);
        btnManageParcel.setBackground(new Color(25, 118, 210));
        btnManageParcel.setForeground(Color.WHITE);
        btnManageParcel.setFocusPainted(false);
        btnManageParcel.setOpaque(true);

        // Track Parcel button
        btnTrackParcel = new JButton("Track Parcel");
        btnTrackParcel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnTrackParcel.setBounds(190, 240, 220, 45);
        btnTrackParcel.setBackground(new Color(46, 125, 50));
        btnTrackParcel.setForeground(Color.WHITE);
        btnTrackParcel.setFocusPainted(false);
        btnTrackParcel.setOpaque(true);

        // Exit button
        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExit.setBounds(190, 300, 220, 45);
        btnExit.setBackground(new Color(198, 40, 40));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setOpaque(true);

        // Add components to panel
        panel.add(lblTitle);
        panel.add(lblWelcome);
        panel.add(btnManageParcel);
        panel.add(btnTrackParcel);
        panel.add(btnExit);

        add(panel);

        // Admin access for Manage Parcel
        btnManageParcel.addActionListener(e -> {

            String password = JOptionPane.showInputDialog(
                    this,
                    "Enter admin password:"
            );

            if (password == null) {
                return;
            }

            if (password.equals("12345")) {

                new ParcelManagementForm().setVisible(true);
                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect admin password.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Open track parcel form
        btnTrackParcel.addActionListener(e -> {

            new TrackParcelForm().setVisible(true);
            dispose();
        });

        // Exit system
        btnExit.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
}