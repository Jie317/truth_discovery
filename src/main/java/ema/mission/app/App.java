package ema.mission.app;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import ema.mission.controller.Bdd;
import ema.mission.controller.GuiControleur;
import ema.mission.model.Excel;
import ema.mission.model.Scraper;
import ema.mission.model.User;
import ema.mission.view.GUI;
import ema.mission.view.Log;

public class App 
{
    public static void main( String[] args )
    {
//    	User u = new User();
//		Log l = new Log(u);
    	testsJie();
//    	testsCharlie();
    }
    
    public static void testsCharlie(){
    	boolean over=false;
    	while(!over){
        	Scanner scan=new Scanner(System.in);
    		System.out.println("Entrez le sujet recherché ");
    		String sujet=scan.nextLine();
    		System.out.println("Entrez la valeur recherchée");
    		String valeur=scan.nextLine();
    		System.out.println("Sur quelle page de Google rechercher ?");
    		int page=scan.nextInt();
    		Map<String, ArrayList<String>> results=Scraper.getResults(sujet, "Born In", valeur, page);
    		for (Map.Entry<String, ArrayList<String>> entry : results.entrySet()){
    			String url=entry.getKey();
    			ArrayList<String> contexts=entry.getValue();
    			System.out.println("\n"+url+"\n\n");
    			for(String context:contexts){
    				System.out.println(context+"\n");
    			}
    			
    		}
    	}
		User u=Bdd.authenticate("charlie.auzet@gmail.com", "sltcava");
    }
    
    public static void testsPierre(){
    	String excel_file = System.getProperty("user.dir") + "/resources/k_top_values_200.xlsx";
    	Excel excel = new Excel(excel_file);
    	excel.readExcel();
    	excel.display(); 	
    }
    
    public static void testsJie(){
    	GuiControleur guiControleur = new GuiControleur();
    	GUI gui = new GUI("BornIn query", guiControleur);
    	guiControleur.setGui(gui);
    	gui.getFrame().setVisible(true);
    
    }
    
}
