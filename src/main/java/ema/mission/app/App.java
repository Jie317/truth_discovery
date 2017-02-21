package ema.mission.app;

import ema.mission.controller.Bdd;
import ema.mission.controller.GuiControleur;
import ema.mission.model.Excel;
import ema.mission.model.User;
import ema.mission.view.GUI;
import ema.mission.view.Log;

public class App 
{
    public static void main( String[] args )
    {
    	User u  = new User();
		Log l = new Log(u);
    	queryGUI(u); 
//    	testsCharlie();
    }
    
    public static void testsCharlie(){

		User u=Bdd.authenticate("charlie.auzet@gmail.com", "sltcava");
    }
    
    public static void testsPierre(){
    	String excel_file = System.getProperty("user.dir") + "/resources/k_top_values_200.xlsx";
    	Excel excel = new Excel(excel_file);
    	excel.readExcel();
    	excel.display(); 	
    }
    
    public static void queryGUI(User u){
    	GuiControleur guiControleur = new GuiControleur(u);
    	GUI gui = new GUI("BornIn query", guiControleur);
    	guiControleur.setGui(gui);
    	gui.getFrame().setVisible(true);
    	guiControleur.onStart();
    	
    
    }
    
}
