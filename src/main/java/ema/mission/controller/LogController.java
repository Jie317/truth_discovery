package ema.mission.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import ema.mission.model.User;
import ema.mission.view.Log;

public class LogController implements ActionListener, KeyListener {

	private Log view;
	private User user;

	public LogController(Log log, User user) {
		this.view = log;
		this.user = user;
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == this.view.getEmail()) {
				this.view.getPass().requestFocusInWindow();
			} else if (e.getSource() == this.view.getPass()) {
				this.view.getBtnConnexion().requestFocusInWindow();
			} else if (e.getSource() == this.view.getBtnConnexion()) {
				this.user = Bdd.authenticate(this.view.getEmail().getText(),
						new String(this.view.getPass().getPassword()));
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		String email=this.view.getEmail().getText();
		String password=new String(this.view.getPass().getPassword());
		if (e.getSource() == this.view.getBtnConnexion()) {
			this.user = Bdd.authenticate(email, password);
			
			if(this.user==null){		
				//Montrer la popup "Cet utilisateur n'existe pas, voulez-vous le créer ?"
				int option = JOptionPane.showConfirmDialog(null, "Cet utilisateur n'existe pas, voulez-vous le créer ?", "Création utilisateur", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.OK_OPTION){
					//Si oui, appelle :
					Bdd.createUser(email, password);	
					//Puis ferme cette vue et ouvre la nouvelle avec :
					this.user=new User(email);
				}
			}else if(this.user.getEmail().equals("WrongPassword")){
				JOptionPane.showMessageDialog(this.view,"Mauvais mot de passe","Erreur mot de passe",JOptionPane.ERROR_MESSAGE);
			}else{
				//Fermer cette vue et ouvrir la nouvelle
			}
		}
	}

	/**
	 * @return the view
	 */
	public Log getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(Log view) {
		this.view = view;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
