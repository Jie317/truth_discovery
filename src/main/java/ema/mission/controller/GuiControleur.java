package ema.mission.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ema.mission.model.Scraper;
import ema.mission.model.SenDetectAndFilter;
import ema.mission.model.User;
import ema.mission.view.GUI;


public class GuiControleur implements ActionListener
{

	final String PAIRESUIVANT = "Paire suivante";
	final String ACCEPTER = "accepter";
	final String REFUSER = "refuser";
	final String CHARGEREXCEL = "Charger Excel";
	static private GUI gui;
	private int userID;
	
	
	String sujet;
	String valeur;
	final int page =1; // default value
	
	Map<String, String> fisrtResultInQueue;
	ArrayList<String> resultList;
	static private ArrayList<Map<String, String> > queueResults = 
			new ArrayList<Map<String, String> >();
	static private ArrayList<String[]> queuePairs = new ArrayList<String[]>();
	static private boolean ready = false;
	static private boolean onStart = true;
	static private boolean updatingExcel=false;
	
	public GuiControleur(User u) 
	{
		super();
		this.userID = u.getUserId();

	}
	
	
	public void onStart() {
		

		new queryResultsQueue().start();
		System.out.println(">>> Thread started...");
		ready = true;	
	}
	
	public class queryResultsQueue extends Thread 
	{

		@Override
		public void run() 
		{
			while (true) 
			{
				if (GuiControleur.queueResults.size() < 5)
				{
					String[] queryPair=null;
					try {
						queryPair = Bdd.getNextUnjudgedPair(userID);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (queryPair == null) 
					{
//						JOptionPane.showMessageDialog(gui.getFrame(), 
//							"Fin de la liste", "", 
//							JOptionPane.INFORMATION_MESSAGE);
						continue;
					}
					
					valeur = queryPair[1];
					sujet = queryPair[0];
					
					GuiControleur.getQueuePairs().add(queryPair);
					
					Map<String, String> pairResults = Scraper.getResults(
							sujet.replace("_", " "), "Born In", 
							valeur.replace("_", " "), page);
					GuiControleur.getQueueResults().add(pairResults);
					System.out.println(">>>> Cached results + : " + 
							GuiControleur.queueResults.size());
				}
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		
		
		if (command.equals(CHARGEREXCEL)) {
		    JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Excel files", "xls", "xlsx");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	final String path=chooser.getSelectedFile().getPath();
				new Thread(){
					@Override
					public void run(){
			            try {
							Bdd.updateValeurs(path);
							Bdd.retrieveUnjudgedPairs(userID);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}.start();
		    }
		}
		if(updatingExcel){
			JOptionPane.showMessageDialog(gui.getFrame(), 
					"Veuillez patienter", "", 
					JOptionPane.INFORMATION_MESSAGE);
			return; 
		}

		// Accepter ou  Refuser
    	if (command.equals(ACCEPTER) || command.equals(REFUSER) ) 
    	{
    		boolean accepted = command.equals(ACCEPTER) ? true: false;
    		int[] indicesChoisis = gui.getResults().getSelectedIndices();
    		String textAccepted="";
    		for (int i: indicesChoisis) 
    		{
    			textAccepted+=resultList.get(i);
    			//storeToDatabase(resultList.get(i), accepted); 
    		}
    		storeToDatabase(textAccepted, accepted);
    	}
		
		// Pair suivant
		if (command.equals(PAIRESUIVANT) || command.equals(ACCEPTER) || command.equals(REFUSER))
		{
			if (!ready || queueResults.isEmpty()) {
				JOptionPane.showMessageDialog(gui.getFrame(), 
						"Veuillez patienter", "", 
						JOptionPane.INFORMATION_MESSAGE);
				return; 
			}
			
			System.out.println("Paire suivante ... ");
			// initilise 
			gui.getListModel().clear();

			// get the search tuple from pair queue
			String[] queryPair = queuePairs.get(0);
			queuePairs.remove(0);

			sujet = queryPair[0];
			valeur = queryPair[1];
			
			gui.getSujet().setText(sujet);
			gui.getValeur().setText(valeur);
			gui.getPage().setText(Integer.toString(page));
						
			// get results from result queue
			fisrtResultInQueue = queueResults.get(0);
			queueResults.remove(0);
			System.out.println(">>>> Cached results - : " + 
					queueResults.size());
			
			if (fisrtResultInQueue.size() == 0) 
			{
				JOptionPane.showMessageDialog(gui.getFrame(), 
					"Rien trouvé pour cette paire.", "", 
					JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			resultList = new ArrayList<String>();
    		for (Entry<String, String> entry: fisrtResultInQueue.entrySet())
    		{
    			String url = entry.getKey();
    			String context = entry.getValue();
    			
    			// detect and filter sentences
    			List<String> sensFiltered = new SenDetectAndFilter().detectAndFilter(context, queryPair);
    			
    			String sensFilteredStr = String.join("<br>", sensFiltered);
    			
				String item = "<html>From URL: " + url + "<br>Context: " 
						+ sensFilteredStr + "<br><br></html>";
				
				resultList.add(String.join("", sensFiltered)); // format to store in the database
				
				System.out.println(item);
				gui.getListModel().addElement(item); // show the URL and context list
    		}
		}
    	//TODO: wrap lines
	}

	private void storeToDatabase(String text, boolean accepted) 
	{
		System.out.println(text);
		System.out.println(userID);
		System.out.println("Storing selected items: " + text);
		try {
			Bdd.addJugement(sujet, valeur, text, userID, accepted);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getACCEPTER()
	{
		return ACCEPTER;
	}

	public String getREFUSER() 
	{
		return REFUSER;
	}

	public void setGui(GUI gui) 
	{
		GuiControleur.gui = gui;
	}

	public static ArrayList<Map<String, String>> getQueueResults() 
	{
		return queueResults;
	}

	public static void setQueueResults(ArrayList<Map<String, String>> queueResults) 
	{
		GuiControleur.queueResults = queueResults;
	}

	public static ArrayList<String[]> getQueuePairs() 
	{
		return queuePairs;
	}

	public static void setQueuePairs(ArrayList<String[]> queuePairs) 
	{
		GuiControleur.queuePairs = queuePairs;
	}

	public String getCHARGEREXCEL() {
		return CHARGEREXCEL;
	}

	public static boolean isOnStart() {
		return onStart;
	}

	public static void setOnStart(boolean onStart) {
		GuiControleur.onStart = onStart;
	}

	public GUI getGui() {
		return gui;
	}

	public String getPAIRESUIVANT() {
		return PAIRESUIVANT;
	}
	


}
