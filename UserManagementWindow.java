import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManagementWindow extends JFrame {
    private JTable adminTable;
    private JTable studentTable;
    private JTable instructorTable;
    private UserTableModel adminTableModel;
    private UserTableModel studentTableModel;
    private UserTableModel instructorTableModel;

    public UserManagementWindow() {
        setTitle("User Management");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fetch users from database and display in tables
        adminTableModel = new UserTableModel(fetchUsers("admin", "admin_id"));
        studentTableModel = new UserTableModel(fetchUsers("student", "student_id"));
        instructorTableModel = new UserTableModel(fetchUsers("instructor", "instructor_id"));

        adminTable = new JTable(adminTableModel);
        studentTable = new JTable(studentTableModel);
        instructorTable = new JTable(instructorTableModel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Admins", new JScrollPane(adminTable));
        tabbedPane.addTab("Students", new JScrollPane(studentTable));
        tabbedPane.addTab("Instructors", new JScrollPane(instructorTable));

        JButton removeButton = new JButton("Remove User");

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUser();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeButton);

        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private Object[][] fetchUsers(String role, String idColumn) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT " + idColumn + ", name, email FROM " + role, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            Object[][] data = new Object[rowCount][3];
            int rowIndex = 0;

            while (rs.next()) {
                data[rowIndex][0] = rs.getInt(idColumn);
                data[rowIndex][1] = rs.getString("name");
                data[rowIndex][2] = rs.getString("email");
                rowIndex++;
            }
            return data;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            return new Object[0][0];
        }
    }

    private void removeUser() {
        int selectedRow = -1;
        String role = "";
        String idColumn = "";
        JTable selectedTable = null;

        if (adminTable.getSelectedRow() != -1) {
            selectedRow = adminTable.getSelectedRow();
            role = "admin";
            idColumn = "admin_id";
            selectedTable = adminTable;
        } else if (studentTable.getSelectedRow() != -1) {
            selectedRow = studentTable.getSelectedRow();
            role = "student";
            idColumn = "student_id";
            selectedTable = studentTable;
        } else if (instructorTable.getSelectedRow() != -1) {
            selectedRow = instructorTable.getSelectedRow();
            role = "instructor";
            idColumn = "instructor_id";
            selectedTable = instructorTable;
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.");
            return;
        }

        int userId = (int) selectedTable.getValueAt(selectedRow, 0);
        String userName = (String) selectedTable.getValueAt(selectedRow, 1);

        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove user " + userName + "?", "Remove User", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM " + role + " WHERE " + idColumn + " = ?");
                ps.setInt(1, userId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User removed successfully.");
                    switch (role) {
                        case "admin":
                            adminTableModel.setData(fetchUsers("admin", "admin_id"));
                            break;
                        case "student":
                            studentTableModel.setData(fetchUsers("student", "student_id"));
                            break;
                        case "instructor":
                            instructorTableModel.setData(fetchUsers("instructor", "instructor_id"));
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No user found with the provided ID.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private class UserTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Name", "Email"};
        private Object[][] data;

        public UserTableModel(Object[][] data) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserManagementWindow().setVisible(true);
        });
    }
}