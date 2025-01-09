import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginUI extends JFrame {
    public LoginUI() {
        setTitle("LMS Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 50, 100, 30);
        JTextField emailField = new JTextField();
        emailField.setBounds(150, 50, 200, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 150, 100, 30);
        String[] roles = {"Admin", "Instructor"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 150, 200, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 200, 100, 30);

        JButton signupButton = new JButton("Signup");
        signupButton.setBounds(260, 200, 100, 30);

        // Action for Login Button
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM " + role.toLowerCase() + " WHERE email = ? AND password = ?");
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful");
                    dispose(); // Close login screen

                    switch (role) {
                        case "Admin":
                            new AdminDashboardUI(rs.getString("name"), role); // Redirect to Admin Dashboard
                            break;
                        case "Instructor":
                            new InstructorDashboardUI(rs.getString("name"), role); // Redirect to Instructor Dashboard
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown role");
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        
        signupButton.addActionListener(e -> {
            dispose();
            new SignupUI().setVisible(true);
        });

        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleComboBox);
        add(loginButton);
        add(signupButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}