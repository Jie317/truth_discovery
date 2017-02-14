package ema.mission.TruthDiscovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

	public static List<String> scrape(String sujet, String predicat, String valeur, int page) {
		Document doc;
		List<String> urls=new ArrayList<String>();

		int counterCity = 0, nbElements = 0;
		String nbPage = page!=1 ? "&start=" + (page-1) * 10 : "";
		String url="https://www.google.com/search?as_q=" + sujet + "+" + predicat
				+ valeur + "&as_eq=&as_nlo=&as_nhi=&lr=lang_en&cr=countryCA&as_qdr=all&as_sitesearch=&as_occt=any&safe=images&tbs=&as_filetype=&as_rights="
				+ nbPage;
		try {

			doc = Jsoup.connect(url).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
			Elements divResult = doc.getElementsByClass("g");
			for (Element e : divResult) {
				String titre = e.getElementsByClass("r").get(0).text();
				String lien = e.getElementsByClass("r").get(0).getElementsByAttribute("href").attr("href")
						.replace("/url?q=", "");
				lien=lien.substring(0, lien.indexOf("&"));
				if (e.getElementsByClass("s").size() != 0) {
					String description = e.getElementsByClass("s").get(0).getElementsByClass("st").get(0).text();
					System.out.println(titre + " " + lien + "\n" + description + "\n");
					if (titre.contains(valeur) || description.contains(valeur)) {
						counterCity++;
						urls.add(lien);
						getContext(lien,valeur);
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
	
	public static void getContext(String url, String textToFind){
		Document doc;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
			Elements elements=doc.select("div:not(:has(div))");

			for(Element element:elements){
				if(element.text().contains(textToFind)){
					System.out.println(element.text());
				}
			}
			
		} catch (IOException e) {
			System.out.println("Yo there's been a problem with loading the page "+url);
		}

	}
}
