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
import javax.swing.JTextField;

public class AddGradesWindow extends JFrame {
    public AddGradesWindow() {
        setTitle("Add Grades");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);

        JLabel courseIdLabel = new JLabel("Course ID:");
        JTextField courseIdField = new JTextField(20);

        JLabel gradeLabel = new JLabel("Grade:");
        JTextField gradeField = new JTextField(20);

        JButton addButton = new JButton("Add Grade");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(studentIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(courseIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(courseIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(gradeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(gradeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addButton, gbc);

        addButton.addActionListener(e -> {
            try {
                int studentId = Integer.parseInt(studentIdField.getText());
                int courseId = Integer.parseInt(courseIdField.getText());
                float grade = Float.parseFloat(gradeField.getText());

                try (Connection con = DBConnection.getConnection()) {
                    // Check if student_id and course_id exist
                    PreparedStatement checkStudent = con.prepareStatement("SELECT * FROM student WHERE student_id = ?");
                    checkStudent.setInt(1, studentId);
                    ResultSet studentRs = checkStudent.executeQuery();

                    PreparedStatement checkCourse = con.prepareStatement("SELECT * FROM course WHERE course_id = ?");
                    checkCourse.setInt(1, courseId);
                    ResultSet courseRs = checkCourse.executeQuery();

                    if (!studentRs.next()) {
                        JOptionPane.showMessageDialog(this, "Student ID does not exist.");
                        return;
                    }

                    if (!courseRs.next()) {
                        JOptionPane.showMessageDialog(this, "Course ID does not exist.");
                        return;
                    }

                    // Insert grade
                    PreparedStatement ps = con.prepareStatement("INSERT INTO Grades (student_id, assignment_id, grade) VALUES (?, ?, ?)");
                    ps.setInt(1, studentId);
                    ps.setInt(2, courseId);
                    ps.setFloat(3, grade);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Grade added successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for Student ID, Course ID, and Grade.");
            }
        });

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AddGradesWindow();
    }
}