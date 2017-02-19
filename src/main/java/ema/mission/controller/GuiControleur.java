package ema.mission.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.util.HSSFColor.TURQUOISE;

import ema.mission.model.Scraper;
import ema.mission.model.User;
import ema.mission.view.GUI;


public class GuiControleur implements ActionListener
{

	final String RECHERCHER = "rechercher";
	final String ACCEPTER = "accepter";
	final String REFUSER = "refuser";
	final String CHARGEREXCEL = "Charger Excel";
	static private GUI gui;
	private int userID;
	private boolean judged = false;
	private boolean onStartJudged = true;
	
	
	String sujet;
	String valeur;
	final int page =1; // default value
	
	Map<String, String> results;
	ArrayList<String> resultList;
	static private ArrayList<Map<String, String> > queueResults = 
			new ArrayList<Map<String, String> >();
	static private ArrayList<String[]> queuePairs = new ArrayList<String[]>();
	static private boolean ready = false;
	static private boolean onStart = true;
	
	public GuiControleur(User u) 
	{
		super();
		this.userID = u.getUserId();
		queryResultsQueue();
	}
	
	private void queryResultsQueue() 
	{
		
		new Thread()
		{
			@Override
			public void run() 
			{
				while (true) 
				{
					boolean debug_mark = false;
					while (GuiControleur.queueResults.size() < 5)
					{
						debug_mark = true;
						String[] queryPair = Bdd.getNextUnjudgedPair(userID);
						
						if (queryPair == null) 
						{
							JOptionPane.showMessageDialog(gui.getFrame(), 
								"Fin de la liste", "", 
								JOptionPane.OK_OPTION);
							return;
						}
						
						valeur = queryPair[1];
						sujet = queryPair[0];
						// show the pair on starting the GUI
						if (GuiControleur.isOnStart()) {
							GuiControleur.gui.getSujet().setText(sujet);
							GuiControleur.gui.getValeur().setText(valeur);
							GuiControleur.setOnStart(false);
						}
						
						GuiControleur.getQueuePairs().add(queryPair);
						
						results = Scraper.getResults(sujet.replace("_", " "), "Born In", valeur.replace("_", " "), page);
						GuiControleur.getQueueResults().add(results);
						
					}
					if (debug_mark) 
					{
						System.out.println("5 results for the next 5 pairs cached.");
					}
				}
			}
		}.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		
		// Rechercher
		if (command.equals(RECHERCHER))
		{
			if (!judged && !onStartJudged) {
				JOptionPane.showMessageDialog(gui.getFrame(), 
						"Veuillez juger la contexte.", "", 
						JOptionPane.INFORMATION_MESSAGE);
			}
			System.out.println("Rechercher ... ");
			// initilise 
			gui.getListModel().clear();
			judged = false;
	
			// check if the result queue is ready
			
			boolean mes = false;
			while (queueResults.size()==0)
			{
				if (!mes) 
				{
					JOptionPane.showMessageDialog(gui.getFrame(), 
						"Veuillez patienter", "", 
						JOptionPane.INFORMATION_MESSAGE);
					mes = true;
				}
			}
			
			// get the search tuple from pair queue
			
			String[] queryPair = queuePairs.get(0);
			queuePairs.remove(0);

			sujet = queryPair[0];
			valeur = queryPair[1];
			
			gui.getSujet().setText(sujet);
			gui.getValeur().setText(valeur);
			gui.getPage().setText(Integer.toString(page));
						
			// get results from result queue
			results = queueResults.get(0);
			queueResults.remove(0);
			
			if (results.size() == 0) 
			{
				JOptionPane.showMessageDialog(gui.getFrame(), 
					"Rien trouvé.", "", 
					JOptionPane.OK_OPTION);
				return;
			}
			
			resultList = new ArrayList<String>();
    		for (Entry<String, String> entry: results.entrySet())
    		{
    			String url = entry.getKey();
    			String context = entry.getValue();
    			
				String item = "<html>From URL: " + url + "<br>Context: " 
						+ context + "<br><br></html>";
				resultList.add(context);
				
				System.out.println(item);
				gui.getListModel().addElement(item);
    		}
		}
		
		// Accepter
    	if (command.equals(ACCEPTER)) 
    	{
    		judged = true;
    		onStartJudged = false;
    		int[] indicesChoisis = gui.getResults().getSelectedIndices();
    		
    		for (int i: indicesChoisis) 
    		{
    			storeToDatabase(resultList.get(i), true); 
    		}
    	}
    		
		// Refuser
    	if (command.equals(REFUSER)) 
    	{
    		judged = true;
    		onStartJudged = false;
    		int[] indicesChoisis1 = gui.getResults().getSelectedIndices();
    		for (int i: indicesChoisis1) 
    		{
    			storeToDatabase(resultList.get(i), false); 
    		}
    	}
		
    	if (command.equals(CHARGEREXCEL)) {
			// Call method here
		}
    	
    	//TODO: wrap lines
		
	}

	private void storeToDatabase(String text, boolean accepted) 
	{
		System.out.println("Storing selected items: " + text);
		Bdd.addJugement(sujet, valeur, text, userID, accepted);
	}

	public String getRECHERCHER() 
	{
		return RECHERCHER;
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
		this.gui = gui;
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

	public static boolean isReady() 
	{
		return ready;
	}

	public static void setReady(boolean ready)
	{
		GuiControleur.ready = ready;
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
	


}
