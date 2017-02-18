package ema.mission.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import ema.mission.controller.GuiControleur;

import javax.swing.JList;

public class GUI {
	private GuiControleur guiControleur;

	private JFrame frame;
	private JTextField sujet;
	private JTextField valeur;
	private JTextField page;
	private JList<String> results;
	private DefaultListModel<String> listModel;
	

	/**
	 * Create the application.
	 * @param guiControleur 
	 * @param string 
	 */
	public GUI(String name, GuiControleur guiControleur) {
		this.guiControleur = guiControleur;
		frame = new JFrame(name);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {	
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 448, 35);
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		JLabel lblNewLabel = new JLabel("Subjet");
		panel.add(lblNewLabel);
		
		sujet = new JTextField();
		panel.add(sujet);
		sujet.setColumns(8);
		
		JLabel lblNewLabel_1 = new JLabel("Valeur");
		panel.add(lblNewLabel_1);
		
		valeur = new JTextField();
		panel.add(valeur);
		valeur.setColumns(8);
		
		JLabel lblPage = new JLabel("Page");
		panel.add(lblPage);
		
		page = new JTextField();
		panel.add(page);
		page.setColumns(3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(1, 40, 448, 37);
		frame.getContentPane().add(panel_1);
		
		JButton btnNewButton = new JButton(guiControleur.getRECHERCHER());
		btnNewButton.addActionListener(guiControleur);
		panel_1.add(btnNewButton);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 233, 448, 37);
		frame.getContentPane().add(panel_3);
		
		JButton btnNewButton_1 = new JButton(guiControleur.getACCEPTER());
		btnNewButton_1.addActionListener(guiControleur);
		panel_3.add(btnNewButton_1);
		
		JButton btnRefuser = new JButton(guiControleur.getREFUSER());
		btnRefuser.addActionListener(guiControleur);
		panel_3.add(btnRefuser);
		
		listModel = new DefaultListModel<>();
		results = new JList<String>(listModel);
		results.setVisibleRowCount(10);
		
		
		JScrollPane showResults = new JScrollPane(results);
		showResults.setBounds(0, 76, 448, 157);
		frame.getContentPane().add(showResults);
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTextField getSujet() {
		return sujet;
	}

	public JTextField getValeur() {
		return valeur;
	}

	public JTextField getPage() {
		return page;
	}

	public JList<String> getResults() {
		return results;
	}

	public DefaultListModel<String> getListModel() {
		return listModel;
	}

	
}
