package ema.mission.TruthDiscovery;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App 
{
    public static void main( String[] args )
    {
    	//String excel_file = System.getProperty("user.dir") + "/resources/k_top_values_200.xlsx";
    	//Excel excel = new Excel(excel_file);
    	//excel.readExcel();
    	
    	boolean over=false;
    	Scanner scan=new Scanner(System.in);
    	while(!over){
    		System.out.println("Entrez le sujet recherché ");
    		String sujet=scan.nextLine();
    		System.out.println("Entrez la valeur recherchée");
    		String valeur=scan.nextLine();
    		System.out.println("Sur quelle page de Google rechercher ?");
    		int page=scan.nextInt();
    		Scraper.scrape(sujet, "BornIn", valeur, page);
    	}
    }
    
}
