package ema.mission.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ema.mission.controller.LogController;
import ema.mission.model.User;

public class Log extends JFrame {

	private User user;
	private LogController controller;

	private final int LARGEUR_FENETRE = 400, HAUTEUR_FENETRE = 200;
	private JButton btnConnexion;
	private JTextField email;
	private JPasswordField pass;

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

		this.setLayout(new GridBagLayout());
		
		JLabel emailLabel = new JLabel("Email: ");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0; // la grille commence en (0, 0)
		gbc.gridx = 0;
		gbc.gridy = 0;
		 /* une seule cellule sera disponible pour ce composant. */
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING; 
		/* marges */
		gbc.insets = new Insets(10, 15, 0, 0);
		this.add(emailLabel,gbc);
		
		email = new JTextField();
		email.addKeyListener(controller);
		email.setPreferredSize(new Dimension(200,25));
		gbc.gridx = 1; /* une position horizontalement à droite de l'étiquette */
		gbc.gridy = 0; /* sur la même ligne que l'étiquette */
		gbc.gridwidth = GridBagConstraints.REMAINDER; /* il est le dernier composant de sa ligne. */
		gbc.gridheight = 1; /* une seule cellule verticalement suffit */
		/* Le composant peut s'étendre sur tout l'espace qui lui est attribué horizontalement. */
		gbc.fill = GridBagConstraints.HORIZONTAL;
		/* Alignons ce composant sur la même ligne d'écriture que son étiquette. */
		gbc.anchor = GridBagConstraints.BASELINE;
		gbc.insets = new Insets(0, 15, 20, 10);
		this.add(email,gbc);
		
		JLabel passLabel = new JLabel("Password: ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		 /* une seule cellule sera disponible pour ce composant. */
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING; 
		/* marges */
		gbc.insets = new Insets(10, 15, 0, 0);
		this.add(passLabel,gbc);
		
		pass = new JPasswordField();
		pass.addKeyListener(controller);
		pass.setPreferredSize(new Dimension(200,25));
		gbc.gridx = 1; /* une position horizontalement à droite de l'étiquette */
		gbc.gridy = 1; /* sur la même ligne que l'étiquette */
		gbc.gridwidth = GridBagConstraints.REMAINDER; /* il est le dernier composant de sa ligne. */
		gbc.gridheight = 1; /* une seule cellule verticalement suffit */
		/* Le composant peut s'étendre sur tout l'espace qui lui est attribué horizontalement. */
		gbc.fill = GridBagConstraints.HORIZONTAL;
		/* Alignons ce composant sur la même ligne d'écriture que son étiquette. */
		gbc.anchor = GridBagConstraints.BASELINE;
		gbc.insets = new Insets(0, 15, 20, 10);
		this.add(pass,gbc);

		// Ajout d'un bouton de connexion
		btnConnexion = new JButton("Connexion");
		btnConnexion.addActionListener(controller);
		btnConnexion.addKeyListener(controller);
		gbc.gridx = 0; /* une position horizontalement à droite de l'étiquette */
		gbc.gridy = 2; /* sur la même ligne que l'étiquette */
		gbc.gridwidth = 3;/* il est le dernier composant de sa ligne. */
		gbc.gridheight = 1; /* une seule cellule verticalement suffit */
		/* Le composant peut s'étendre sur tout l'espace qui lui est attribué horizontalement. */
		gbc.fill = GridBagConstraints.HORIZONTAL;
		/* Alignons ce composant sur la même ligne d'écriture que son étiquette. */
		gbc.anchor = GridBagConstraints.BASELINE;
		gbc.insets = new Insets(0, 15, 0, 10);
		this.add(btnConnexion, gbc);

		// Packing et affichage de la JFrame
		this.pack();
		this.setVisible(true);
	}

	/**
	 * @return the controller
	 */
	public LogController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(LogController controller) {
		this.controller = controller;
	}

	/**
	 * @return the btnConnexion
	 */
	public JButton getBtnConnexion() {
		return btnConnexion;
	}

	/**
	 * @param btnConnexion the btnConnexion to set
	 */
	public void setBtnConnexion(JButton btnConnexion) {
		this.btnConnexion = btnConnexion;
	}

	/**
	 * @return the email
	 */
	public JTextField getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(JTextField email) {
		this.email = email;
	}

	/**
	 * @return the pass
	 */
	public JPasswordField getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(JPasswordField pass) {
		this.pass = pass;
	}

}
