import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InstructorDashboardUI extends JFrame {
    public InstructorDashboardUI(String userName, String userRole) {
        setTitle("Instructor Dashboard - Welcome, " + userName + " (" + userRole + ")");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setLayout(new GridBagLayout());

        JLabel headerLabel = new JLabel("Instructor Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        headerPanel.add(headerLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        JButton manageCoursesButton = new JButton("Manage Courses");
        JButton viewReportsButton = new JButton("View Reports");
        JButton updateProfileButton = new JButton("Update Profile");
        JButton logoutButton = new JButton("Logout");
        JButton addGradesButton = new JButton("Add Grades");

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(manageCoursesButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(viewReportsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(updateProfileButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        buttonPanel.add(addGradesButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        buttonPanel.add(logoutButton, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        manageCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CourseManagementWindow().setVisible(true);
            }
        });

        
        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateProfileWindow(userName).setVisible(true);
            }
        });

        addGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddGradesWindow().setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginUI().setVisible(true);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new InstructorDashboardUI("Instructor Name", "Instructor");
    }
}