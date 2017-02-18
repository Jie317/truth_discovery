package ema.mission.controller;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import ema.mission.model.Scraper;
import ema.mission.view.GUI;



public class GuiControleur implements ActionListener{
	final String RECHERCHER = "rechercher";
	final String ACCEPTER = "accepter";
	final String REFUSER = "refuser";
	private GUI gui;
	
	String sujet = "Picasso";
	String valeur = "Malaga";
	int page =1;
	
	Map<String, ArrayList<String>> results;
	ArrayList<String> resultList;
	ArrayList<String> accepted;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		// Rechercher
		if (command.equals(RECHERCHER)) {
			System.out.println("Rechercher ... ");
			gui.getListModel().clear();

	
			// get the search tuple from database
			// example
			sujet = "Picasso";
			valeur = "Malaga";
			page =1;
			
			gui.getSujet().setText(sujet);
			gui.getValeur().setText(valeur);
			gui.getPage().setText(Integer.toString(page));
						
			// Afficher les résultats
			results = Scraper.getResults(sujet, "Born In", valeur, page);
			
			if (results.size() == 0) {
				JOptionPane.showMessageDialog(gui.getFrame(), "Rien trouvé.",
					"", JOptionPane.OK_OPTION);
				
			}
			
			resultList = new ArrayList<String>();
    		for (Map.Entry<String, ArrayList<String>> entry: results.entrySet()){
    			String url = entry.getKey();
    			ArrayList<String> contexts = entry.getValue();
    			
       			for(String context: contexts){
    				String item = "<html>From URL: " + url + "<br>Context: " 
    						+ context + "<br><br></html>";
    				resultList.add(item);
    				
    				System.out.println(item);
    				gui.getListModel().addElement(item);
    				
    				
    			}	
    		}
		}
		
		// Accepter
    	if (command.equals(ACCEPTER)) {
    		System.out.println("Accepting selected items...");
    		accepted = new ArrayList<String>();
    		int[] indicesChoisis = gui.getResults().getSelectedIndices();
    		
    		for (int i: indicesChoisis) {
    			System.out.println(i);
    			gui.getListModel().remove(i);
    			accepted.add(resultList.get(i));
    			storeToDatabase(accepted); 
    		}
    	}
    		
		// Refuser
    	if (command.equals(REFUSER)) {
    		System.out.println("Deleting refused items...");
    		int[] indicesChoisis1 = gui.getResults().getSelectedIndices();
    		for (int i: indicesChoisis1) {
    			gui.getListModel().remove(i);
    		}
    	}
		
    	//TODO: wrap lines
		
	}

	private void storeToDatabase(ArrayList<String> accepted2) {
		// TODO Auto-generated method stub
		System.out.println("Storing selected paragraphs...");
		
	}

	public void setGui(GUI gui) {
		this.gui = gui;
		gui.getSujet().setText(sujet);
		gui.getValeur().setText(valeur);
		gui.getPage().setText(Integer.toString(page));
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
	


}
