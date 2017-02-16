package ema.mission.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ema.mission.controller.LogController;
import ema.mission.model.User;

public class Log extends JFrame {

	private User user;
	private LogController controller;

	private final int LARGEUR_FENETRE = 600, HAUTEUR_FENETRE = 600;
	private JPanel panel;
	private JButton btnConnexion;

	public Log(User u) {
		this.user = u;
		controller = new LogController(this, user);

		// Centrage de la fenêtre et choix de la taille de la fenêtre
		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		this.setPreferredSize(new Dimension(this.LARGEUR_FENETRE, this.HAUTEUR_FENETRE));
		int windowLeftPosition = screenSize.width / 2 - this.LARGEUR_FENETRE / 2;
		int windowRightPostion = screenSize.height / 2 - this.HAUTEUR_FENETRE / 2;
		this.setLocation(windowLeftPosition, windowRightPostion);

		// Empêche la fenêtre d'être redimensionnée
		setResizable(false);

		// Choix du titre
		this.setTitle("Connexion");

		// Enregistrement de l'option EXIT_ON_CLOSE lors de la fermeture de la
		// fenêtre (arrêt du procéssus)
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//Ajout d'un panel qui contiendra tout
		panel = new JPanel();
		panel.setBounds(0, 0, 594, 571);
		GridLayout gl = new GridLayout(4, 3);
		gl.setHgap(5); //Cinq pixels d'espace entre les colonnes (H comme Horizontal)
		gl.setVgap(5); //Cinq pixels d'espace entre les lignes (V comme Vertical) 
		panel.setLayout(gl);
		this.getContentPane().add(panel);

		// Ajout d'un bouton de connexion
		btnConnexion = new JButton("Connexion");
		btnConnexion.setBounds(227, 330, 142, 23);
		btnConnexion.setBackground(Color.RED);
		btnConnexion.setForeground(Color.WHITE);
		btnConnexion.addActionListener(controller);
		panel.add(btnConnexion);

		// Packing et affichage de la JFrame
		this.pack();
		this.setVisible(true);
	}

}
