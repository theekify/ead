import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDashboardUI extends JFrame {
    private JPanel mainPanel;
    private JButton enrollCoursesButton;
    private JButton viewGradesButton;
    private JButton updateProfileButton;
    private JButton logoutButton;

    public StudentDashboardUI() {
        setTitle("Student Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));

        enrollCoursesButton = new JButton("Enroll in Courses");
        viewGradesButton = new JButton("View Grades");
        updateProfileButton = new JButton("Update Profile");
        logoutButton = new JButton("Logout");

        mainPanel.add(enrollCoursesButton);
        mainPanel.add(viewGradesButton);
        mainPanel.add(updateProfileButton);
        mainPanel.add(logoutButton);

        add(mainPanel);

        enrollCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEnrollCoursesWindow();
            }
        });

        viewGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openViewGradesWindow();
            }
        });

        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUpdateProfileWindow();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }

    private void openEnrollCoursesWindow() {
        // Code to open EnrollCoursesWindow
        JOptionPane.showMessageDialog(null, "Enroll Courses Window opened.");
    }

    private void openViewGradesWindow() {
        // Code to open ViewGradesWindow
        JOptionPane.showMessageDialog(null, "View Grades Window opened.");
    }

    private void openUpdateProfileWindow() {
        // Code to open UpdateProfileWindow
        JOptionPane.showMessageDialog(null, "Update Profile Window opened.");
    }

    private void logout() {
        // Code to handle logout
        JOptionPane.showMessageDialog(null, "Logged out successfully.");
        dispose();
        // Code to return to the login screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentDashboardUI().setVisible(true);
            }
        });
    }
}