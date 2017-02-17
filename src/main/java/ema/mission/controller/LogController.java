package ema.mission.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
		if (e.getSource() == this.view.getBtnConnexion()) {
			this.user = Bdd.authenticate(this.view.getEmail().getText(), new String(this.view.getPass().getPassword()));
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
