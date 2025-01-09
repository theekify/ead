import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManagementWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private JButton removeButton;

    public UserManagementWindow() {
        setTitle("User Management");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Admin Tab
        JPanel adminPanel = new JPanel(new BorderLayout());
        JTable adminTable = new JTable(fetchUsers("admin", "admin_id"), new String[]{"ID", "Name", "Email"});
        adminPanel.add(new JScrollPane(adminTable), BorderLayout.CENTER);
        tabbedPane.addTab("Admin", adminPanel);

        // Instructor Tab
        JPanel instructorPanel = new JPanel(new BorderLayout());
        JTable instructorTable = new JTable(fetchUsers("instructor", "instructor_id"), new String[]{"ID", "Name", "Email"});
        instructorPanel.add(new JScrollPane(instructorTable), BorderLayout.CENTER);
        tabbedPane.addTab("Instructor", instructorPanel);

        removeButton = new JButton("Remove User");
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
        setLocationRelativeTo(null);
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
                data[rowIndex][0] = rs.getString(idColumn);
                data[rowIndex][1] = rs.getString("name");
                data[rowIndex][2] = rs.getString("email");
                rowIndex++;
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    private void removeUser() {
        // Implementation for removing a user
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserManagementWindow().setVisible(true);
        });
    }
}