package ema.mission.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mindrot.jbcrypt.BCrypt;

import ema.mission.model.Couple;
import ema.mission.model.Excel;
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
		
		String checkExistQuery="SELECT * FROM Users WHERE email=?";
		try{
			preparedStatement=conn.prepareStatement(checkExistQuery);
			preparedStatement.setString(1, email);
			ResultSet rs=preparedStatement.executeQuery();
			if(rs.first()){
				String pwd=rs.getString("password");
				boolean connected=BCrypt.checkpw(password, pwd);
				if(connected){
					u=new User(email);
				}else{
					u=new User("WrongPassword");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return u;
	}
	
	public static User createUser(String email, String password){
		
		PreparedStatement preparedStatement=null;
		User u=null;
		Connection conn=ConnectDB();
		
		String query="INSERT INTO Users (email, password) VALUES (?,?)";
		
		String hashPass=BCrypt.hashpw(password, BCrypt.gensalt());
		try{
			preparedStatement=conn.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, hashPass);
			preparedStatement.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return u;
	}
	
	public static void updateValeurs(){
		ArrayList<String> sujetsAlreadyInDb=new ArrayList<>();
		ArrayList<String> valeursAlreadyInDB= new ArrayList<>();

    	String excel_file = System.getProperty("user.dir") + "/resources/k_top_values_200.xlsx";
    	Excel excel = new Excel(excel_file);
    	Map<String, List<Couple>> excelResults=excel.readExcel();
    	
		PreparedStatement preparedStatement=null;

		Connection conn=ConnectDB();
		String getValeursQuery="SELECT * FROM Valeurs";
		String insertValeurQuery="INSERT INTO Valeurs (Sujet, Valeur) VALUES (?,?)";
		
		try{
			preparedStatement=conn.prepareStatement(getValeursQuery);
			ResultSet rs=preparedStatement.executeQuery();
			while(rs.next()){
				String sujet=rs.getString("Sujet");
				String valeur=rs.getString("Valeur");
				sujetsAlreadyInDb.add(sujet);
				valeursAlreadyInDB.add(valeur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		for (Entry<String, List<Couple>> entry : excelResults.entrySet()){
			String sujet=entry.getKey();
			List<Couple> couples=entry.getValue();
			for(Couple couple:couples){
				String valeur=couple.getValeur();
				String confiance=couple.getConfiance();
				if(!sujetsAlreadyInDb.contains(sujet) && !valeursAlreadyInDB.contains(valeur)){
					try {
						PreparedStatement preparedStatement2=conn.prepareStatement(insertValeurQuery);
						preparedStatement2.setString(1, sujet);
						preparedStatement2.setString(2, valeur);
						preparedStatement2.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
	}
	
	public static String[][] getFirstUnjudgedValues(int userId, int numberOfResults){
		PreparedStatement preparedStatement=null;
		PreparedStatement preparedStatement2=null;
		String subject="";
		String value="";
		String[][] ret=new String[numberOfResults][2];
		
		Connection conn=ConnectDB();
		String query="SELECT * FROM Jugements WHERE Id_User=?";
		String query2="SELECT * FROM Valeurs";
		ArrayList<Integer> alreadySeen=new ArrayList<Integer>();
		
		try {
			preparedStatement=conn.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			ResultSet rs=preparedStatement.executeQuery();
			while(rs.next()){
				alreadySeen.add(rs.getInt("Id_Valeur"));
			}
			
			preparedStatement2=conn.prepareStatement(query2);
			ResultSet rs2=preparedStatement2.executeQuery();
			int i=0;
			while(rs2.next() && i<5){
				int idValeur=rs2.getInt("Id");
				if(!alreadySeen.contains(idValeur)){
					subject=rs2.getString("Sujet");
					value=rs2.getString("Valeur");
					ret[i][0]=subject;
					ret[i][1]=value;
					i++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String[] getFirstUnjudgedValue(int userId){
		PreparedStatement preparedStatement=null;
		PreparedStatement preparedStatement2=null;
		String subject="";
		String value="";
		String[] ret=new String[2];
		
		Connection conn=ConnectDB();
		String query="SELECT * FROM Jugements WHERE Id_User=?";
		String query2="SELECT * FROM Valeurs";
		ArrayList<Integer> alreadySeen=new ArrayList<Integer>();
		
		try {
			preparedStatement=conn.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			ResultSet rs=preparedStatement.executeQuery();
			while(rs.next()){
				alreadySeen.add(rs.getInt("Id_Valeur"));
			}
			
			preparedStatement2=conn.prepareStatement(query2);
			ResultSet rs2=preparedStatement2.executeQuery();
			while(rs2.next()){
				int idValeur=rs2.getInt("Id");
				if(!alreadySeen.contains(idValeur)){
					subject=rs2.getString("Sujet");
					value=rs2.getString("Valeur");
					ret[0]=subject;
					ret[1]=value;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void addJugement(String sujet, String valeur, String text, int idUser , boolean accepted){
		PreparedStatement getValeurIdQuery=null;
		PreparedStatement insertJugementQuery=null;
		Connection conn=ConnectDB();
		int valeurId=0;
		
		String valeurIdQuery="SELECT Id FROM Valeurs WHERE Sujet=? AND Valeur=?";
		String jugementQuery="INSERT INTO Jugements (Id_Valeur, Accepted, Text, Id_User) VALUES (?,?,?,?)";

		try {
			getValeurIdQuery=conn.prepareStatement(valeurIdQuery);
			getValeurIdQuery.setString(1, sujet);
			getValeurIdQuery.setString(2, valeur);
			ResultSet rs=getValeurIdQuery.executeQuery();
			if(rs.first()){
				valeurId=rs.getInt("Id");			
			}
			
			insertJugementQuery=conn.prepareStatement(jugementQuery);
			insertJugementQuery.setInt(1, valeurId);
			insertJugementQuery.setBoolean(2, accepted);
			insertJugementQuery.setString(3, text);
			insertJugementQuery.setInt(4, idUser);
			insertJugementQuery.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
}
