package ema.mission.model;

public class User {

	private String email;
	private int userId;
	
	public User(String email){
		this.setEmail(email);
	}
	
	public User(String email, int userId){
		this.email=email;
		this.userId=userId;
	}
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * Permet de rajouter un utilisateur
	 * 
	 * @param email - String
	 */
	public void AddUser(String email) {
		
	}
	
	/**
	 * Permet d'afficher un utlisateur / fonction de test
	 */
	public void affichage(){
		System.out.println("Email: "+this.email);
	}
	
	

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
