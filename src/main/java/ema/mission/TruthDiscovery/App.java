package ema.mission.TruthDiscovery;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App 
{
    public static void main( String[] args )
    {
    	scrape("Picasso", "BornIn");
    }
    
    public static void scrape(String sujet, String predicat){
	    Document doc;
	    try{
	        doc =Jsoup.connect("https://www.google.com/search?as_q="+sujet+"+"+predicat+"&as_eq=&as_nlo=&as_nhi=&lr=lang_en&cr=countryCA&as_qdr=all&as_sitesearch=&as_occt=any&safe=images&tbs=&as_filetype=&as_rights=").userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
	        Elements divResult=doc.getElementsByClass("");
	        System.out.println(doc.getElementById("res"));
	    }
	    
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
