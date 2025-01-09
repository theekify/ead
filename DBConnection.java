import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private DBConnection() {}

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Update the URL, username, and password to match your database credentials
            String url = "jdbc:mysql://localhost:3306/LMS"; // Use your database name
            String username = "root"; // Replace with your database username
            String password = "orypubit"; // Replace with your database password

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();
        if (con != null) {
            System.out.println("Connection test successful.");
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Connection test failed.");
        }
    }
}