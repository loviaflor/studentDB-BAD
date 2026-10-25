package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	public static final String URL = "jdbc:mysql://localhost:3306/studentdatabase";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "";
	
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("Database Connected!");
		} catch (SQLException e) {
			System.out.println("Database Connection Failed!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Manager not found.");
			e.printStackTrace();
		}
		return connection;
	}
	
	public void disconnect(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("Database closed.");
			} catch (SQLException e) {
				System.out.println("failed to close the database.");
				e.printStackTrace();
			}
		}
	}
	


}
