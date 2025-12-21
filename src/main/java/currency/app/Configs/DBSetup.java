package currency.app.Configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSetup {
    public Connection getConnection() throws java.sql.SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String uname = "java_user";
            String pass = "java_password";
            String url = "jdbc:postgresql://localhost:5432/currency_exchange";
            return DriverManager.getConnection(url, uname, pass); // Establishes the connection to the database
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
