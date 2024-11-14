package com.mycompany.registrationform;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JFrame {

    private final JTextField txtID;
    private final JTextField txtName;
    private final JTextField txtAddress;
    private final JTextField txtContact;
    private final JRadioButton maleButton;
    private final JRadioButton femaleButton;
    private final JTable table;
    private final DefaultTableModel model;
    private final ButtonGroup genderGroup;

    public RegistrationForm() {
        setTitle("Registration Form");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLUE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Left side of the form
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);
        txtID = new JTextField(10);
        gbc.gridx = 1;
        add(txtID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        txtName = new JTextField(10);
        gbc.gridx = 1;
        add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Gender:"), gbc);
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        gbc.gridx = 1;
        add(genderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Address:"), gbc);
        txtAddress = new JTextField(10);
        gbc.gridx = 1;
        add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Contact:"), gbc);
        txtContact = new JTextField(10);
        gbc.gridx = 1;
        add(txtContact, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton btnRegister = new JButton("Register");
        add(btnRegister, gbc);

        gbc.gridx = 1;
        JButton btnExit = new JButton("Exit");
        add(btnExit, gbc);

        // Right side of the form (table)
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Gender");
        model.addColumn("Address");
        model.addColumn("Contact");
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 7;
        gbc.anchor = GridBagConstraints.NORTH;
        add(scrollPane, gbc);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }

    private void registerUser() {
        String id = txtID.getText();
        String name = txtName.getText();
        String gender = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : "");
        String address = txtAddress.getText();
        String contact = txtContact.getText();

        if (id.isEmpty() || name.isEmpty() || gender.isEmpty() || address.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_db", "root", "Admin24")) {
            String query = "INSERT INTO users (ID, Name, Gender, Address, Contact) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, gender);
            stmt.setString(4, address);
            stmt.setString(5, contact);
            stmt.executeUpdate();

            model.addRow(new Object[]{id, name, gender, address, contact});

            txtID.setText("");
            txtName.setText("");
            txtAddress.setText("");
            txtContact.setText("");
            genderGroup.clearSelection();

            JOptionPane.showMessageDialog(this, "User registered successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm().setVisible(true));
    }
}
