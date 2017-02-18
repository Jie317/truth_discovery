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
	
	Map<String, ArrayList<String>> results;
	ArrayList<String> resultList;
	ArrayList<String> accepted;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		// Rechercher
		if (command.equals(RECHERCHER)) {
			System.out.println("Rechercher ... ");
			gui.getListModel().removeAllElements();
			String sujet = gui.getSujet().getText();
			String valeur = gui.getValeur().getText();
			String pageStr = gui.getPage().getText();
			
			if (sujet.equals("") || valeur.equals("") || pageStr.equals("")) {
				JOptionPane.showMessageDialog(gui.getFrame(), "Les entrées ne sont pas valides.",
					"", JOptionPane.OK_OPTION);
				return;
			}
			int page = Integer.parseInt(pageStr);
			
			// Afficher les résultats
			results = Scraper.getResults(sujet, "Born In", valeur, page);
			System.out.println(results.size());
			resultList = new ArrayList<String>();
    		for (Map.Entry<String, ArrayList<String>> entry: results.entrySet()){
    			String url = entry.getKey();
    			ArrayList<String> contexts = entry.getValue();
    			
       			for(String context: contexts){
    				String item = "<html>" + url + "<br>" + context + "</html>";
    				resultList.add(item);
    				
    				System.out.println(item);
    				gui.getListModel().addElement(item);
    				
    				JTextArea newItem = new JTextArea();
    				newItem.setText(item);
    				gui.getResults().add(newItem);
    			}	
    		}
		}
		
		// Accepter
    	if (command.equals(ACCEPTER)) {
    		System.out.println("Accepting selected items...");
    		accepted = new ArrayList<String>();
    		int[] indicesChoisis = gui.getResults().getSelectedIndices();
    		for (int i: indicesChoisis) {
    			gui.getResults().remove(i);
    			accepted.add(resultList.get(i));
    			storeToDatabase(accepted); 
    		}
    	}
    		
		// Refuser
    	if (command.equals(REFUSER)) {
    		System.out.println("Deleting refused items...");
    		int[] indicesChoisis1 = gui.getResults().getSelectedIndices();
    		for (int i: indicesChoisis1) {
    			gui.getResults().remove(i);
    		}
    	}
			
		
	}

	private void storeToDatabase(ArrayList<String> accepted2) {
		// TODO Auto-generated method stub
		System.out.println("Storing selected paragraphs...");
		
	}

	public void setGui(GUI gui) {
		this.gui = gui;
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
