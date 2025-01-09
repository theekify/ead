import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseManagementWindow extends JFrame {
    private JTable courseTable;
    private CourseTableModel courseTableModel;

    public CourseManagementWindow() {
        setTitle("Course Management");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fetch courses from database and display in a table
        courseTableModel = new CourseTableModel(fetchCourses());
        courseTable = new JTable(courseTableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);

        JButton addButton = new JButton("Add Course");
        JButton editButton = new JButton("Edit Course");
        JButton deleteButton = new JButton("Delete Course");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCourse();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCourse();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Object[][] fetchCourses() {
        // Fetch course data from database
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT course_id, course_name, instructor_id FROM Courses", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            Object[][] data = new Object[rowCount][3];
            int rowIndex = 0;
            while (rs.next()) {
                data[rowIndex][0] = rs.getString("course_id");
                data[rowIndex][1] = rs.getString("course_name");
                data[rowIndex][2] = rs.getString("instructor_id");
                rowIndex++;
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    private void addCourse() {
        JTextField courseIdField = new JTextField();
        JTextField courseNameField = new JTextField();
        JComboBox<String> instructorIdComboBox = new JComboBox<>(fetchInstructorIds());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Course ID:"));
        panel.add(courseIdField);
        panel.add(new JLabel("Course Name:"));
        panel.add(courseNameField);
        panel.add(new JLabel("Instructor ID:"));
        panel.add(instructorIdComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String courseId = courseIdField.getText();
            String courseName = courseNameField.getText();
            String instructorId = (String) instructorIdComboBox.getSelectedItem();

            // Save course to database
            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Courses (course_id, course_name, instructor_id) VALUES (?, ?, ?)");
                ps.setString(1, courseId);
                ps.setString(2, courseName);
                ps.setString(3, instructorId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course added successfully!");
                // Refresh the table
                courseTableModel.setData(fetchCourses());
                courseTableModel.fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void editCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit.");
            return;
        }

        String courseId = (String) courseTableModel.getValueAt(selectedRow, 0);
        String courseName = (String) courseTableModel.getValueAt(selectedRow, 1);
        String instructorId = (String) courseTableModel.getValueAt(selectedRow, 2);

        JTextField courseIdField = new JTextField(courseId);
        JTextField courseNameField = new JTextField(courseName);
        JComboBox<String> instructorIdComboBox = new JComboBox<>(fetchInstructorIds());
        instructorIdComboBox.setSelectedItem(instructorId);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Course ID:"));
        panel.add(courseIdField);
        panel.add(new JLabel("Course Name:"));
        panel.add(courseNameField);
        panel.add(new JLabel("Instructor ID:"));
        panel.add(instructorIdComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newCourseId = courseIdField.getText();
            String newCourseName = courseNameField.getText();
            String newInstructorId = (String) instructorIdComboBox.getSelectedItem();

            // Update course in database
            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("UPDATE Courses SET course_id = ?, course_name = ?, instructor_id = ? WHERE course_id = ?");
                ps.setString(1, newCourseId);
                ps.setString(2, newCourseName);
                ps.setString(3, newInstructorId);
                ps.setString(4, courseId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course updated successfully!");
                // Refresh the table
                courseTableModel.setData(fetchCourses());
                courseTableModel.fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            return;
        }

        String courseId = (String) courseTableModel.getValueAt(selectedRow, 0);

        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this course?", "Delete Course", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            // Delete course from database
            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM Courses WHERE course_id = ?");
                ps.setString(1, courseId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                // Refresh the table
                courseTableModel.setData(fetchCourses());
                courseTableModel.fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private String[] fetchInstructorIds() {
        ArrayList<String> instructorIds = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT instructor_id FROM instructor");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                instructorIds.add(rs.getString("instructor_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instructorIds.toArray(new String[0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CourseManagementWindow().setVisible(true);
        });
    }

    class CourseTableModel extends AbstractTableModel {
        private String[] columnNames = {"Course ID", "Course Name", "Instructor ID"};
        private Object[][] data;

        public CourseTableModel(Object[][] data) {
            this.data = data;
        }

        public void setData(Object[][] data) {
            this.data = data;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    }
}