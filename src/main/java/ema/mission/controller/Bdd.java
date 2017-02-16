package ema.mission.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Bdd {
	
	public static Connection ConnectDB() {

		Connection conn = null;
		// Récupérer le Driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Ok !");
		} catch (Exception e) {
			System.out.println("erreur");
		}

		if (conn == null) {
			try {
				conn = DriverManager.getConnection("jdbc:mysql://51.254.124.54:3306/TruthDiscovery", "Truth",
						"QqxVYbH6Jfa42L4h");
				System.out.println("Okk !");

			} catch (SQLException ex) {

				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		return conn;
	}
}
