package parcelmanagementsystem;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:parcel_management.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTable() {

        //Set not null means must be fill
        String sql = """
                CREATE TABLE IF NOT EXISTS parcel (
                    tracking_no TEXT PRIMARY KEY,
                    sender_name TEXT NOT NULL,
                    receiver_name TEXT NOT NULL,
                    weight REAL NOT NULL,
                    parcel_type TEXT NOT NULL,
                    status TEXT NOT NULL,
                    delivery_fee REAL NOT NULL
                )
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

            System.out.println("Table Parcel is ready.");

        } catch (SQLException e) {

            System.out.println("Database Error: " + e.getMessage());
        }
    }
}