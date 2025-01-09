import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UpdateInstructorProfileWindow extends JFrame {
    private String userName;

    public UpdateInstructorProfileWindow(String userName) {
        this.userName = userName;

        setTitle("Update Profile");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel currentNameLabel = new JLabel("Current Name:");
        JLabel currentNameValue = new JLabel();
        JLabel nameLabel = new JLabel("New Name:");
        JTextField nameField = new JTextField(20);

        JLabel currentEmailLabel = new JLabel("Current Email:");
        JLabel currentEmailValue = new JLabel();
        JLabel emailLabel = new JLabel("New Email:");
        JTextField emailField = new JTextField(20);

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        JLabel currentPasswordValue = new JLabel();
        JLabel passwordLabel = new JLabel("New Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton updateButton = new JButton("Update");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(currentNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(currentNameValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(currentEmailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(currentEmailValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(currentPasswordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(currentPasswordValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(updateButton, gbc);

        // Fetch current user details
        fetchUserDetails(currentNameValue, currentEmailValue, currentPasswordValue);

        updateButton.addActionListener(e -> {
            String newName = nameField.getText().isEmpty() ? currentNameValue.getText() : nameField.getText();
            String newEmail = emailField.getText().isEmpty() ? currentEmailValue.getText() : emailField.getText();
            String newPassword = new String(passwordField.getPassword()).isEmpty() ? currentPasswordValue.getText() : new String(passwordField.getPassword());

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("UPDATE instructor SET name = ?, email = ?, password = ? WHERE name = ?");
                ps.setString(1, newName);
                ps.setString(2, newEmail);
                ps.setString(3, newPassword);
                ps.setString(4, userName);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
                
                fetchUserDetails(currentNameValue, currentEmailValue, currentPasswordValue); // Refresh the current details
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        add(formPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchUserDetails(JLabel currentNameValue, JLabel currentEmailValue, JLabel currentPasswordValue) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("SELECT name, email, password FROM instructor WHERE name = ?");
            ps.setString(1, userName);
            rs = ps.executeQuery();

            if (rs.next()) {
                currentNameValue.setText(rs.getString("name"));
                currentEmailValue.setText(rs.getString("email"));
                currentPasswordValue.setText(rs.getString("password"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}