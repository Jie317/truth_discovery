package ema.mission.TruthDiscovery;

public class Couple {
	
	private String valeur;
	private float confiance;
	
	public Couple(String valeur ,float confiance){
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
	public float getConfiance() {
		return confiance;
	}
	/**
	 * @param confiance the confiance to set
	 */
	public void setConfiance(float confiance) {
		this.confiance = confiance;
	} 

}
