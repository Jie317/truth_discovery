package ema.mission.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import ema.mission.model.Scraper;
import ema.mission.model.User;
import ema.mission.view.GUI;


public class GuiControleur implements ActionListener
{

	final String RECHERCHER = "rechercher";
	final String ACCEPTER = "accepter";
	final String REFUSER = "refuser";
	private GUI gui;
	private int userID;
	
	String sujet;
	String valeur;
	final int page =1; // default value
	
	Map<String, String> results;
	ArrayList<String> resultList;
	static private ArrayList<Map<String, String> > queueResults = 
			new ArrayList<Map<String, String> >();
	static private ArrayList<String[]> queuePairs = new ArrayList<String[]>();
	static private boolean ready = false;
	
	
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
						String[] queryPair = Bdd.getFirstUnjudgedValue(userID);
						
						if (queryPair == null) 
						{
							JOptionPane.showMessageDialog(gui.getFrame(), 
								"Fin de la liste", "", 
								JOptionPane.OK_OPTION);
							return;
						}
						
						GuiControleur.getQueuePairs().add(queryPair);
						sujet = queryPair[0];
						valeur = queryPair[1];
						
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
			System.out.println("Rechercher ... ");
			gui.getListModel().clear();
	
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
    		int[] indicesChoisis = gui.getResults().getSelectedIndices();
    		
    		for (int i: indicesChoisis) 
    		{
    			storeToDatabase(resultList.get(i), true); 
    		}
    	}
    		
		// Refuser
    	if (command.equals(REFUSER)) 
    	{
    		int[] indicesChoisis1 = gui.getResults().getSelectedIndices();
    		for (int i: indicesChoisis1) 
    		{
    			storeToDatabase(resultList.get(i), false); 
    		}
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
	


}
