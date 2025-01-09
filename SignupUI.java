import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SignupUI extends JFrame {
    public SignupUI() {
        setTitle("LMS Signup");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 30);
        JTextField nameField = new JTextField();
        nameField.setBounds(150, 50, 200, 30);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 100, 100, 30);
        JTextField emailField = new JTextField();
        emailField.setBounds(150, 100, 200, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 150, 100, 30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 150, 200, 30);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 200, 100, 30);
        String[] roles = {"Admin", "Instructor"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 200, 200, 30);

        JButton signupButton = new JButton("Signup");
        signupButton.setBounds(150, 250, 100, 30);

        JButton backButton = new JButton("Back");
        backButton.setBounds(260, 250, 100, 30);

        // Action for Signup Button
        signupButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            saveUserToDatabase(name, email, password, role);
        });

        // Action for Back Button
        backButton.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleComboBox);
        add(signupButton);
        add(backButton);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveUserToDatabase(String name, String email, String password, String role) {
        String tableName = "";
        switch (role) {
            case "Admin":
                tableName = "admin";
                break;
            case "Instructor":
                tableName = "instructor";
                break;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " (name, email, password) VALUES (?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Signup Successful");
            dispose(); // Close signup screen

            // Redirect to the appropriate dashboard
            switch (role) {
                case "Admin":
                    new AdminDashboardUI(name, role); // Redirect to Admin Dashboard
                    break;
                case "Instructor":
                    new InstructorDashboardUI(name, role); // Redirect to Instructor Dashboard
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Unknown role");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SignupUI().setVisible(true);
        });
    }
}