package ema.mission.TruthDiscovery;

public class Couple {
	
	private String valeur;
	private String confiance;
	
	public Couple(String valeur ,String confiance){
		this.valeur=valeur;
		this.confiance=confiance;
	}
	
	
	/**
	 * @return the valeur
	 */
	public String getValeur() {
		return valeur;
	}
	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	/**
	 * @return the confiance
	 */
	public String getConfiance() {
		return confiance;
	}
	/**
	 * @param confiance the confiance to set
	 */
	public void setConfiance(String confiance) {
		this.confiance = confiance;
	} 

	public String toString(){
		return("valeur: " +this.valeur+" confiance: "+this.confiance+"-");
	}
}
