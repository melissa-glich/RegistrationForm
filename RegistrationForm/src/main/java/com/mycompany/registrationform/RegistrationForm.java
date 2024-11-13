package com.mycompany.registrationform;

/**
 *
 * @author Melissa
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JFrame {
    // Components for the form
    private JTextField nameField, mobileField;
    private JRadioButton maleRadio, femaleRadio;
    private JTextField dobField;
    private JTextArea addressField;
    private JCheckBox termsCheck;
    private JLabel resultLabel;
    private ButtonGroup genderGroup; // Moved to class-level for reset

    public RegistrationForm() {
        initComponents();
    }

    private void initComponents() {
        // Frame properties
        setTitle("Registration Form");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        JLabel mobileLabel = new JLabel("Mobile:");
        mobileField = new JTextField(15);

        JLabel genderLabel = new JLabel("Gender:");
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        JLabel dobLabel = new JLabel("DOB (YYYY-MM-DD):");
        dobField = new JTextField(10);

        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextArea(3, 20);
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);

        termsCheck = new JCheckBox("Accept Terms and Conditions.");

        JButton submitButton = new JButton("Submit");
        JButton resetButton = new JButton("Reset");

        resultLabel = new JLabel();

        // Set layout
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(mobileLabel);
        panel.add(mobileField);
        panel.add(genderLabel);

        JPanel genderPanel = new JPanel();
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        panel.add(genderPanel);

        panel.add(dobLabel);
        panel.add(dobField);
        panel.add(addressLabel);
        panel.add(new JScrollPane(addressField));
        panel.add(termsCheck);

        panel.add(submitButton);
        panel.add(resetButton);

        // Add listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });

        // Add to frame
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
    }

    private void submitForm() {
        // Collect data from fields
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String gender = maleRadio.isSelected() ? "Male" : (femaleRadio.isSelected() ? "Female" : "");
        String dob = dobField.getText();
        String address = addressField.getText();

        if (!termsCheck.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please accept the terms and conditions.");
            return;
        }

        // Validate input (simple validation)
        if (name.isEmpty() || mobile.isEmpty() || gender.isEmpty() || dob.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // Connect to the database and insert data
        try {
            // Load MySQL driver if necessary
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_db", "root", "Admin24");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)")) {

                stmt.setString(1, name);
                stmt.setString(2, mobile);
                stmt.setString(3, gender);
                stmt.setDate(4, Date.valueOf(dob)); // Ensure dob is in YYYY-MM-DD format
                stmt.setString(5, address);
                stmt.executeUpdate();

                resultLabel.setText("Registration Successful!");
                displayData(name, mobile, gender, dob, address);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Driver not found.");
        }
    }

    private void displayData(String name, String mobile, String gender, String dob, String address) {
        // Display user data on the right side of the form
        resultLabel.setText("<html>Name: " + name + "<br>Mobile: " + mobile + "<br>Gender: " + gender + "<br>DOB: " + dob + "<br>Address: " + address + "</html>");
    }

    private void resetForm() {
        // Reset all fields
        nameField.setText("");
        mobileField.setText("");
        genderGroup.clearSelection();
        dobField.setText("");
        addressField.setText("");
        termsCheck.setSelected(false);
        resultLabel.setText("");
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(() -> {
            new RegistrationForm().setVisible(true);
        });
    }
}
