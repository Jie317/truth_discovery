package ema.mission.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import ema.mission.model.Scraper;
import ema.mission.model.User;
import ema.mission.view.GUI;


public class GuiControleur implements ActionListener{

	final String RECHERCHER = "rechercher";
	final String ACCEPTER = "accepter";
	final String REFUSER = "refuser";
	private GUI gui;
	private User user;
	
	String sujet;
	String valeur;
	int page =1; // default value
	
	Map<String, String> results;
	ArrayList<String> resultList;
	
	
	public GuiControleur(User user) {
		super();
		this.user = user;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		// Rechercher
		if (command.equals(RECHERCHER)) {
			System.out.println("Rechercher ... ");
			gui.getListModel().clear();
	
			// get the search tuple from database
			// example
			String[] queryPair = Bdd.getFirstUnjudgedValue(this.user.getUserId());
			
			if (queryPair.length != 2) {
				JOptionPane.showMessageDialog(gui.getFrame(), 
					"Paire de requête non-valide", "", 
					JOptionPane.OK_OPTION);
				return;
			}
			sujet = queryPair[0];
			valeur = queryPair[1];
			
			gui.getSujet().setText(sujet);
			gui.getValeur().setText(valeur);
			gui.getPage().setText(Integer.toString(page));
						
			// Afficher les résultats
			results = Scraper.getResults(sujet.replace("_", " "), "Born In", valeur, page);
			
			if (results.size() == 0) {
				JOptionPane.showMessageDialog(gui.getFrame(), 
					"Rien trouvé.", "", 
					JOptionPane.OK_OPTION);
				return;
			}
			
			resultList = new ArrayList<String>();
    		for (Map.Entry<String, String> entry: 
    				results.entrySet()){
    			String url = entry.getKey();
    			String context = entry.getValue();
    			
    			if(context.equals(""))
    				continue;
    			
    			String item = "<html>From URL: " + url + "<br>Context: " 
    					+ context + "<br><br></html>";
    			
    			resultList.add(context);
    				
    			System.out.println(item);
    			gui.getListModel().addElement(item);
    			
    		}
		}
		
		// Accepter
    	if (command.equals(ACCEPTER)) {
    		System.out.println("Accepting selected items...");
    		int[] indicesChoisis = gui.getResults().getSelectedIndices();
    		
    		for (int i: indicesChoisis) {
    			System.out.println(i);
    			gui.getListModel().remove(i);
    		}
    		// On store tout le texte, pas les paragraphes séparés
			storeToDatabase(resultList.toString(), true); 
    	}
    		
		// Refuser
    	if (command.equals(REFUSER)) {
    		System.out.println("Deleting refused items...");
    		int[] indicesChoisis1 = gui.getResults().getSelectedIndices();
    		for (int i: indicesChoisis1) {
    			gui.getListModel().remove(i);
    		}
    		//Même s'il refuse, il a quand même émis un jugement, donc on le store
    		storeToDatabase(resultList.toString(), false);
    	}
		
    	//TODO: wrap lines
		
	}

	private void storeToDatabase(String text, boolean accepted) {
		System.out.println("Storing selected paragraph: " + text);
		Bdd.addJugement(sujet, valeur, text, this.user.getUserId(), accepted);
	}

	public String getRECHERCHER() {
		return RECHERCHER;
	}

	public String getACCEPTER() {
		return ACCEPTER;
	}

	public String getREFUSER() {
		return REFUSER;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}
	


}
