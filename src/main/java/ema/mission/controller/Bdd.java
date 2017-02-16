package ema.mission.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ema.mission.model.User;

public class Bdd {
	/**
	 * Méthode de connexion à la base de données.
	 * @return Connection
	 * */
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

	/**
	 * Méthode pour authentifier un utilisateur.
	 * @return User
	 * */
	public static User authenticate(String email, String password){
		
		Connection conn=null;
		PreparedStatement preparedStatement = null;
		User u=null;
		String query="SELECT * FROM Users WHERE email=? AND password=?";
		conn= ConnectDB();
		
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.first()){
				u=new User(email);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return u;
	}
}
