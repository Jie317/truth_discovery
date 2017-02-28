package ema.mission.model;

import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * @author Jie
 * This class detects and filters the sentences. The method returns the three
 * sentences around the target pair if the pair is in the same sentence, 
 * otherwise it returns all the sentences between the two entities in the pair.
 *
 */
public class SenDetectAndFilter
{
	
	private static SentenceDetectorME detector;

	public SenDetectAndFilter()  
	{
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("resources/en-sent.bin");
			SentenceModel model = new SentenceModel(inputStream);
			detector = new SentenceDetectorME(model);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> detectAndFilter(String article, String[] pair) 
	{
		List<String> sensFiltered = new ArrayList<String>();
		if (article.isEmpty()) 
		{
			return sensFiltered;
		}
		
		String sujet = pair[0].replace("_", " ");
		String valeur = pair[1].replace("_", " ");
		String[] sens = detector.sentDetect(article);

		int senLen = sens.length;
		
		// when both object and value are in the same sentence
		for (int i = 0; i < senLen; i++) 
		{
			if ( sens[i].contains(sujet) &&  sens[i].contains(valeur)) 
			{
				if (i-1 >= 0)
				{
					sensFiltered.add(sens[i-1]);
				}
				sensFiltered.add(sens[i]);
				if (i+1 < senLen) 
				{
					sensFiltered.add(sens[i+1]);
				}
				
				return sensFiltered;
			}
		}
		
		// when subject and value are in individual sentences
		for (int i = 0; i < senLen; i++) 
		{
			if (sens[i].contains(sujet)) 
			{
				sensFiltered.add(sens[i]);
				
				for (int j = i+1; j < senLen; j++) 
				{
					sensFiltered.add(sens[j]);
					if (sens[j].contains(valeur)) 
					{
						return sensFiltered;
					}
					sensFiltered.clear();
				}
			}
				
			if (sens[i].contains(valeur)) 
			{
				sensFiltered.add(sens[i]);
				
				for (int j = i+1; j < senLen; j++) 
				{
					sensFiltered.add(sens[j]);
					if (sens[j].contains(valeur)) 
					{
						return sensFiltered;
					}
					sensFiltered.clear();
				}
			}
		}
		return sensFiltered;
	}
	
	// test
	public static void main(String[] args)
	{
		String teString = "Hold on … don’t comment yet. I am fully aware that "
				+ "Picasso was born in Malaga in southern Spain in 1881, that "
				+ "he started his artistic career in Barcelona and remained"
				+ " proudly Spanish all his life. But the reopening of the "
				+ "Musée Picasso in Paris last autumn confirmed the French "
				+ "capital’s unique claim on this stupendously creative painter"
				+ ", sculptor and poet. His genius is so tangled up with the"
				+ " streets and garrets, palaces and attics of Paris, a city"
				+ " that he first visited in 1900 and whose artistic life he "
				+ "would take to new heights.";


		List<String> sens = new SenDetectAndFilter().detectAndFilter(teString, new String[]{"mkefenf", "Malaga"});
		for (String s: sens)
		{
			System.out.println("\n\nget:"+s);
		}
	}
	
}
