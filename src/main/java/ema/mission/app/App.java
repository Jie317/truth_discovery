package ema.mission.app;

import ema.mission.model.User;
import ema.mission.view.Log;

public class App 
{
    public static void main( String[] args )
    {
		new Log(new User());
//    	testQueryGUI(new User("jie@gmail.com", 8)); 
//    	testsCharlie();
    }
    
    
    
    // test
//    public static void testsCharlie(){
//
//		try {
//			User u=Bdd.authenticate("charlie.auzet@gmail.com", "sltcava");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//    
//    public static void testsPierre(){
//    	String excel_file = System.getProperty("user.dir") + "/resources/k_top_values_200.xlsx";
//    	Excel excel = new Excel(excel_file);
//    	excel.readExcel();
//    	excel.display(); 	
//    }
//    
//    public static void testQueryGUI(User u){
//    	GuiControleur guiControleur = new GuiControleur(u);
//    	GUI gui = new GUI("BornIn query", guiControleur);
//    	guiControleur.setGui(gui);
//    	gui.getFrame().setVisible(true);
//    	guiControleur.onStart();
//    	
//    
//    }
    
}
