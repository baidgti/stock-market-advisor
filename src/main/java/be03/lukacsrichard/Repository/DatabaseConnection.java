package be03.lukacsrichard.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection connection;

    public DatabaseConnection() throws SQLException {
        openConnection();
    }

    // Method to open a new connection
    private void openConnection() throws SQLException {
        this.connection = DriverManager.getConnection(
            "jdbc:sqlserver://localhost;databaseName=StockMarketAdvisor;encrypt=false;",
            "java", 
            "java"
        );
    }

    // Method to retrieve the connection
    public Connection getConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            openConnection();
        }
        return this.connection;
    }

    // Method to close the connection
    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null; // Set to null after closing
            } catch (SQLException e) {
                System.err.println("Failed to close database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Method to reopen the connection
    public void reopenConnection() {
        closeConnection();
        try {
            openConnection();
        } catch (SQLException e) {
            System.err.println("Failed to reopen database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
