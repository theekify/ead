import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                data[rowIndex][0] = rs.getInt("course_id");
                data[rowIndex][1] = rs.getString("course_name");
                data[rowIndex][2] = rs.getInt("instructor_id");
                rowIndex++;
            }
            return data;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            return new Object[0][0];
        }
    }

    private void addCourse() {
        JTextField courseNameField = new JTextField();
        JTextField instructorIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Course Name:"));
        panel.add(courseNameField);
        panel.add(new JLabel("Instructor ID:"));
        panel.add(instructorIdField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String courseName = courseNameField.getText();
            int instructorId = Integer.parseInt(instructorIdField.getText());

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Courses (course_name, instructor_id) VALUES (?, ?)");
                ps.setString(1, courseName);
                ps.setInt(2, instructorId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course added successfully.");
                courseTableModel.setData(fetchCourses()); // Refresh the table data
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void editCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit.");
            return;
        }

        int courseId = (int) courseTableModel.getValueAt(selectedRow, 0);
        String currentCourseName = (String) courseTableModel.getValueAt(selectedRow, 1);
        int currentInstructorId = (int) courseTableModel.getValueAt(selectedRow, 2);

        JTextField courseNameField = new JTextField(currentCourseName);
        JTextField instructorIdField = new JTextField(String.valueOf(currentInstructorId));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Course Name:"));
        panel.add(courseNameField);
        panel.add(new JLabel("Instructor ID:"));
        panel.add(instructorIdField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newCourseName = courseNameField.getText();
            int newInstructorId = Integer.parseInt(instructorIdField.getText());

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("UPDATE Courses SET course_name = ?, instructor_id = ? WHERE course_id = ?");
                ps.setString(1, newCourseName);
                ps.setInt(2, newInstructorId);
                ps.setInt(3, courseId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course updated successfully.");
                courseTableModel.setData(fetchCourses()); // Refresh the table data
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void deleteCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            return;
        }

        int courseId = (int) courseTableModel.getValueAt(selectedRow, 0);
        String courseName = (String) courseTableModel.getValueAt(selectedRow, 1);

        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the course " + courseName + "?", "Delete Course", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM Courses WHERE course_id = ?");
                ps.setInt(1, courseId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course deleted successfully.");
                courseTableModel.setData(fetchCourses()); // Refresh the table data
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private class CourseTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Course ID", "Course Name", "Instructor ID"};
        private Object[][] data;

        public CourseTableModel(Object[][] data) {
            this.data = data;
        }

        public void setData(Object[][] data) {
            this.data = data;
            fireTableDataChanged();
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

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}