package fr.uga.info;

import java.util.HashMap;

/**
 * Classe qui sert à stocker les données.
 * 
 * @author Mickaël Turnel
 */
public class Stockage {
	/**
	 * Taille limite en mémoire qu'on doit prendre.
	 */
	static final long TAILLE_LIMITE = 2000000;
	
	/**
	 * Map servant à associer un id à un objet.
	 */
	private HashMap<String, Object> map;
	
	/**
	 * Contructeur par défaut.
	 */
	public Stockage() {
		map = new HashMap<>();
	}
	
	/**
	 * Ajoute un objet en mémoire associé à un id.
	 * 
	 * @param id Id de l'objet à mettre en mémoire.
	 * @param o L'objet à mettre en mémoire.
	 */
	public void ajouterObjet(String id, Object o) {
		// TODO: Tester la place en mémoire de la map
		// Et si elle dépasse la taille limite, on enlève le plus vieux
		map.put(id, o);
	}
	
	
	/**
	 * Récupère un objet en mémoire.
	 * 
	 * @param id Id de l'objet à récupérer.
	 * @return L'objet à récupérer.
	 */
	public Object recupererObjet(String id) {
		// TODO: Si l'object n'est pas en mémoire, vérifier si il est
		// sauvegarder dans le système de fichier
		return map.get(id);
	}
	
	/**
	 * Renvoi le nombre d'objet stocké en mémoire.
	 * 
	 * @return
	 */
	public int nombreObjet() {
		return map.size();
	}
}
