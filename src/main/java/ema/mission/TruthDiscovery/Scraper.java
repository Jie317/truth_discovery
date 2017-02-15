package ema.mission.TruthDiscovery;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

	static ArrayList<String> banList=new ArrayList<String>(){
		private static final long serialVersionUID = -7265231822387638069L;
	{
		add("tripadvisor");
		add("expedia");
	}};
	
	
	public static Map<String, ArrayList<String>> getResults(String sujet, String predicat, String valeur, int page){
		Map<String, ArrayList<String>> results=new HashMap<String,ArrayList<String>>();
		List<String> urls=scrape(sujet,"BornIn",valeur,page);
		
		for(String url:urls){
			ArrayList<String> context=getContext(url, sujet, valeur);
			results.put(url, context);
		}
		return results;
	}
	
	public static List<String> scrape(String sujet, String predicat, String valeur, int page) {
		Document doc;
		List<String> urls=new ArrayList<String>();

		int counterCity = 0, nbElements = 0;
		String nbPage = page!=1 ? "&start=" + (page-1) * 10 : "";
		String url="https://www.google.com/search?as_q=" + sujet + "+" + predicat
				+ "+" + valeur + "&as_eq=&as_nlo=&as_nhi=&lr=lang_en&cr=countryCA&as_qdr=all&as_sitesearch=&as_occt=any&safe=images&tbs=&as_filetype=&as_rights="
				+ nbPage;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
			Elements divResult = doc.getElementsByClass("g");
			
			loop:
			for (Element e : divResult) {
				String titre = e.getElementsByClass("r").get(0).text();
				String lien = e.getElementsByClass("r").get(0).getElementsByAttribute("href").attr("href")
						.replace("/url?q=", "");
				lien=lien.substring(0, lien.indexOf("&"));
				for(String ban:banList){
					if(lien.toLowerCase().contains(ban)){
						continue loop;
					}
				}

				if (e.getElementsByClass("s").size() != 0) {
					String description = e.getElementsByClass("s").get(0).getElementsByClass("st").get(0).text();
					if (titre.contains(valeur) || description.contains(valeur)) {
						counterCity++;
						urls.add(lien);
					}
					nbElements++;
				}
			}
			Double percentage = (double) counterCity / nbElements * 100;
		}

		catch (IOException e) {
			System.out.println("Yo there's been a problem with loading the page "+url);
		}

		return urls;
	}
	
	public static ArrayList<String> getContext(String url, String sujet, String value){
		Document doc;
		ArrayList<String> results=new ArrayList<String>();
		
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
			Elements elements=doc.body().select("*");

			
			for(Element element:elements){
				String textToMatch=Normalizer.normalize(element.ownText(), Normalizer.Form.NFD)
						   .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
						   .toLowerCase();
				
				if(textToMatch.contains(value.toLowerCase()) && textToMatch.contains(sujet.toLowerCase())){
				   	results.add(element.ownText());
				}
			}
			
		} catch (IOException e) {
			System.out.println("Yo there's been a problem with loading the page "+url);
		}
		
		return results;
	}
}
