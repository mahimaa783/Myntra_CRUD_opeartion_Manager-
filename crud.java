import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MyNtraCRUD {
    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost/my_ntra_db";
    static final String USER = "username";
    static final String PASS = "password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {

            boolean running = true;
            while (running) {
                System.out.println("Select an operation:");
                System.out.println("1. Create");
                System.out.println("2. Read");
                System.out.println("3. Update");
                System.out.println("4. Delete");
                System.out.println("5. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        createRecord(conn, scanner);
                        break;
                    case 2:
                        readRecords(conn);
                        break;
                    case 3:
                        updateRecord(conn, scanner);
                        break;
                    case 4:
                        deleteRecord(conn, scanner);
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createRecord(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter name:");
        String name = scanner.nextLine();

        System.out.println("Enter email:");
        String email = scanner.nextLine();

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Record created successfully!");
            }
        }
    }

    private static void readRecords(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
            }
        }
    }

    private static void updateRecord(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter user ID to update:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter new name:");
        String newName = scanner.nextLine();

        System.out.println("Enter new email:");
        String newEmail = scanner.nextLine();

        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setInt(3, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Record updated successfully!");
            }
        }
    }

    private static void deleteRecord(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter user ID to delete:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Record deleted successfully!");
            }
        }
    }
}

