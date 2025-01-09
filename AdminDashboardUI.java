import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AdminDashboardUI extends JFrame {
    private String userName;
    private String userRole;

    public AdminDashboardUI(String userName, String userRole) {
        this.userName = userName;
        this.userRole = userRole;

        setTitle("Admin Dashboard - Welcome, " + userName + " (" + userRole + ")");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setLayout(new GridBagLayout());

        Font headerFont = new Font("Sans-Serif", Font.BOLD, 24);
        Font subHeaderFont = new Font("Sans-Serif", Font.PLAIN, 18);

        JLabel welcomeLabel = new JLabel("Welcome, " + userName + "!");
        welcomeLabel.setFont(headerFont);
        welcomeLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel("Your Role: " + userRole);
        roleLabel.setFont(subHeaderFont);
        roleLabel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        headerPanel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        headerPanel.add(roleLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        Font buttonFont = new Font("Sans-Serif", Font.PLAIN, 16);

        JButton userManagementButton = new JButton("User Management");
        userManagementButton.setFont(buttonFont);
        JButton manageCoursesButton = new JButton("Manage Courses");
        manageCoursesButton.setFont(buttonFont);
        JButton viewReportsButton = new JButton("View Reports");
        viewReportsButton.setFont(buttonFont);
        JButton updateProfileButton = new JButton("Update Profile");
        updateProfileButton.setFont(buttonFont);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(buttonFont);

        userManagementButton.addActionListener(new ButtonClickListener());
        manageCoursesButton.addActionListener(new ButtonClickListener());
        viewReportsButton.addActionListener(new ButtonClickListener());
        updateProfileButton.addActionListener(new ButtonClickListener());
        logoutButton.addActionListener(new ButtonClickListener());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        buttonPanel.add(userManagementButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(manageCoursesButton, gbc);

        gbc.gridy = 2;
        buttonPanel.add(viewReportsButton, gbc);

        gbc.gridy = 3;
        buttonPanel.add(updateProfileButton, gbc);

        gbc.gridy = 4;
        buttonPanel.add(logoutButton, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            try {
                switch (command) {
                    case "User Management":
                        openUserManagementWindow();
                        break;
                    case "Manage Courses":
                        openCourseManagementWindow();
                        break;
                    case "View Reports":
                        openReportsWindow();
                        break;
                    case "Update Profile":
                        openUpdateProfileWindow();
                        break;
                    case "Logout":
                        logout();
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown command: " + command);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void openUserManagementWindow() {
            // Code to open UserManagementWindow
            new UserManagementWindow();
        }

        private void openCourseManagementWindow() {
            // Code to open CourseManagementWindow
            new CourseManagementWindow();
        }

        private void openReportsWindow() {
            // Code to open ReportsWindow
            JOptionPane.showMessageDialog(null, "Reports Window opened.");
        }

        private void openUpdateProfileWindow() {
            // Code to open UpdateProfileWindow
            new UpdateProfileWindow(userName);
        }

        private void logout() {
            // Code to handle logout
            JOptionPane.showMessageDialog(null, "Logged out successfully.");
            dispose();
            new LoginUI();
        }
    }
}