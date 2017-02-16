package ema.mission.TruthDiscovery;

import ema.mission.TruthDiscovery.Excel;

public class App 
{
    public static void main( String[] args )
    {
    	String excel_file = System.getProperty("user.dir") + "/resources/k_top_values_200.xlsx";
    	Excel excel = new Excel(excel_file);
    	excel.readExcel();
    	excel.display();
    	
//    	boolean over=false;
//    	while(!over){
//        	Scanner scan=new Scanner(System.in);
//    		System.out.println("Entrez le sujet recherché ");
//    		String sujet=scan.nextLine();
//    		System.out.println("Entrez la valeur recherchée");
//    		String valeur=scan.nextLine();
//    		System.out.println("Sur quelle page de Google rechercher ?");
//    		int page=scan.nextInt();
//    		Map<String, ArrayList<String>> results=Scraper.getResults(sujet, "Born In", valeur, page);
//    		for (Map.Entry<String, ArrayList<String>> entry : results.entrySet()){
//    			String url=entry.getKey();
//    			ArrayList<String> contexts=entry.getValue();
//    			System.out.println("\n"+url+"\n\n");
//    			for(String context:contexts){
//    				System.out.println(context+"\n");
//    			}
//    			
//    		}
//
//    	}
    }
    
}
