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

import org.apache.poi.ss.formula.functions.Index;
import org.mindrot.jbcrypt.BCrypt;

import com.mysql.cj.api.jdbc.Statement;

import ema.mission.model.Couple;
import ema.mission.model.Excel;
import ema.mission.model.User;

public class Bdd {
	
	// to store all the unjudged pairs
	static ArrayList<String[]> unjudgedPairs = new ArrayList<String[]>();
	
	/**
	 * Méthode de connexion à la base de données.
	 * @return Connection
	 * @throws SQLException 
	 * */
	public static Connection ConnectDB() throws SQLException {

		Connection conn = null;
		// Récupérer le Driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Ok !");
		} catch (Exception e) {
			System.out.println("erreur");
		}

		if (conn == null) {

				conn = DriverManager.getConnection("jdbc:mysql://51.254.124.54:3306/TruthDiscovery", "Truth",
						"QqxVYbH6Jfa42L4h");
				System.out.println("Okk !");

		}
		return conn;
	}

	/**
	 * Méthode pour authentifier un utilisateur.
	 * @return User
	 * @throws SQLException 
	 * */
	public static User authenticate(String email, String password) throws SQLException{
		
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
				int id=rs.getInt("Id");
				boolean connected=BCrypt.checkpw(password, pwd);
				if(connected){
					u=new User(email, id);
				}else{
					u=new User("WrongPassword");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return u;
	}
	
	public static User createUser(String email, String password) throws SQLException{
		
		PreparedStatement preparedStatement=null;
		User u=null;
		Connection conn=ConnectDB();
		
		String query="INSERT INTO Users (email, password) VALUES (?,?)";
		
		String hashPass=BCrypt.hashpw(password, BCrypt.gensalt());
		try{
			preparedStatement=conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, hashPass);
			preparedStatement.executeUpdate();
			ResultSet generatedKeys=preparedStatement.getGeneratedKeys();
			if(generatedKeys.next()){
				u=new User(email, generatedKeys.getInt("Id"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return u;
	}
	
	public static void updateValeurs() throws SQLException{
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
	
	public static String[][] getFirstUnjudgedValues(int userId, int numberOfResults) throws SQLException{
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
			while(rs2.next() && i<numberOfResults){
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
	
	public static String[] getFirstUnjudgedValue(int userId) throws SQLException{
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
	
//	 a generator to yield next unjudged pair, consisting of the 
//	following two methods:
	
	public static String[] getNextUnjudgedPair(int userId) throws SQLException {
		if (unjudgedPairs.isEmpty()) {
			System.out.println(">>>> Retriveing unjudged pair list");
			retrieveUnjudgedPairs(userId); 
		}
		int index = NumberGenerator.next();
		if (index >= unjudgedPairs.size()) {
			return null;
		}
		return unjudgedPairs.get(index);
	}
	
	public static void retrieveUnjudgedPairs(int userId) throws SQLException{
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
					unjudgedPairs.add(new String[]{subject, value});
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addJugement(String sujet, String valeur, String text, int idUser , boolean accepted) throws SQLException{
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
			if (text.length() >= 10000){
				text = text.substring(0, 10000);
			}
			insertJugementQuery.setString(3, text);
			insertJugementQuery.setInt(4, idUser);
			insertJugementQuery.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
	
	// index generator to return continuous natural number from 0
	public static class NumberGenerator
	{
		static private int i = 0;
	 
		static public int next()
        {
			return i++;
        }
	}
}
