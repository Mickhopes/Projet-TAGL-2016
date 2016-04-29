package fr.uga.info;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Classe qui sert à stocker les données.
 * 
 * @author Mickaël Turnel
 * @author Line Pouvaret
 */
public class Stockage {
	/**
	 * Taille limite d'objets en mémoire.
	 */
	static final int TAILLE_LIMITE = 1000;
	
	/**
	 * Attribut contenant le nombre d'objets en mémoire.
	 */
	private int tailleOccupee;
	
	/**
	 * Map servant à associer un id à un objet.
	 */
	private HashMap<String, Object> map;
	
	/**
	 * Contructeur par défaut.
	 */
	public Stockage() {
		map = new HashMap<>();
		tailleOccupee = 0;
	}
	
	/**
	 * Ajoute un objet en mémoire associé à un id.
	 * 
	 * @param id Id de l'objet à mettre en mémoire.
	 * @param o L'objet à mettre en mémoire.
	 */
	public synchronized void ajouterObjet(String id, Object o) {
		if (tailleOccupee == TAILLE_LIMITE) {
			sauvegarderPlusAncien();
		}
		
		map.put(id, o);
		tailleOccupee++;
	}
	
	
	/**
	 * Récupère un objet en mémoire.
	 * 
	 * @param id Id de l'objet à récupérer.
	 * @return L'objet à récupérer, null si in n'existe pas.
	 */
	public synchronized Object recupererObjet(String id) {
		if (!map.containsKey(id)) {
			chargerObjet(id);
		}
		
		return map.get(id);
	}
	
	/**
	 * Supprime un objet en mémoire.
	 * 
	 * @param id Id de l'objet à supprimer.
	 * @return true si l'objet à été supprimé, false sinon.
	 */
	public synchronized boolean supprimerObjet(String id) {
		// TODO: Si l'object n'est pas en mémoire, vérifier si il est
		// sauvegarder dans le système de fichier
		if (map.containsKey(id)) {
			if (map.get(id) instanceof LinkedList<?>) {
				tailleOccupee -= ((LinkedList<Object>)map.get(id)).size();
			}
			
			map.remove(id);
			return true;
		}
		return false;
	}
	
	/**
	 * Ajoute l'object dans la liste renseignée par "id".
	 * Si la liste n'existe pas, elle est crée.
	 * 
	 * @param id L'id de la liste.
	 * @param o L'object à ajouter.
	 * @param fin true pour ajouter à la fin de la liste, false au début.
	 * @return -1 si l'object renseigné par "id" n'est pas une liste, sinon la nouvelle taille de la liste.
	 */
	public synchronized int ajoutListeObject(String id, Object o, boolean fin) {
		Object liste = recupererObjet(id);
		
		// Si on a null, la liste n'existe pas il faut la créer.
		if (liste == null) {
			LinkedList<Object> listeObjet = new LinkedList<Object>();
			if (fin) {
				listeObjet.addLast(o);
			} else {
				listeObjet.addFirst(o);
			}
			ajouterObjet(id, listeObjet);
			tailleOccupee ++;
			return listeObjet.size();
		} else {
			// On vérifie d'abords que l'objet est bien une liste
			if (liste instanceof LinkedList<?>) {
				LinkedList<Object> listeObjet = (LinkedList<Object>)liste;
				if (fin) {
					listeObjet.addLast(o);
				} else {
					listeObjet.addFirst(o);
				}
				tailleOccupee++;
				return listeObjet.size();
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * Récupère les object dans la liste renseignée par "id".
	 * 
	 * @param id L'id de la liste.
	 * @param debut Index de début des objets à récupérer.
	 * @param fin Index de fin des objets à récupérer.
	 * @return La sous liste des objets à récupérer.
	 */
	public synchronized List<Object> recupererListeObject(String id, int debut, int fin) {
		Object liste = recupererObjet(id);
		
		if (liste != null) {
			// On vérifie que notre objet est bien une liste
			if (liste instanceof LinkedList<?>) {
				LinkedList<Object> listeObjet = (LinkedList<Object>)liste;
				if (fin == -1) {
					return listeObjet;
				} else {
					if (fin == -1) {
						List<Object> l = new ArrayList<Object>();
						for(int i = 0; i < listeObjet.size(); i++) {
							l.add(listeObjet.get(i));
						}
						return l;
					}
					
					fin = fin >= listeObjet.size() ? listeObjet.size()-1 : fin;
					if (fin < debut) {
						return null;
					} else {
						List<Object> l = new ArrayList<Object>();
						
						for(int i = debut; i <= fin; i++) {
							l.add(listeObjet.get(i));
						}
						return l;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Supprime l'object dans la liste renseignée par "id".
	 * 
	 * @param id L'id de la liste.
	 * @param first true si on supprime le premier, false le dernier.
	 * @return L'objet supprimé, null si la liste n'existe pas ou que l'objet n'est pas une liste.
	 */
	public synchronized Object supprimerListeObject(String id, boolean first) {
		Object liste = recupererObjet(id);
		
		// Si on a null, la liste n'existe pas il faut la créer.
		if (liste != null) {
			// On vérifie qu'on utilise une liste
			if (liste instanceof LinkedList<?>) {
				LinkedList<Object> listeObjet = (LinkedList<Object>)liste;
				tailleOccupee--;
				if (first) {
					return listeObjet.removeFirst();
				} else {
					return listeObjet.removeLast();
				}
			}
		}
		return null;
	}
	
	/**
	 * Renvoi le nombre d'objet stocké en mémoire.
	 * 
	 * @return
	 */
	public synchronized int nombreObjet() {
		return map.size();
	}
	
	/**
	 * Renvoi le nombre d'objet de la liste stocké en mémoire.
	 * 
	 * @return Le nombre d'object de la liste, -1 si la liste n'existe pas, -2 si l'objet n'est pas une liste.
	 */
	public synchronized int nombreObjetListe(String id) {
		Object liste = recupererObjet(id);
		if (liste != null) {
			if (liste instanceof LinkedList<?>) {
				LinkedList<Object> listeObject = (LinkedList<Object>) liste;
				return listeObject.size();
			}
			return -2;
		}
		return -1;
	}
	
	private void sauvegarderPlusAncien() {
		try (FileWriter fichier = new FileWriter("object_list.txt", true)) {
			Entry<String, Object> e = map.entrySet().iterator().next();
			if (e.getValue() instanceof LinkedList<?>) {
				fichier.write(e.getKey() + ";");
				LinkedList<Object> l = (LinkedList<Object>)e.getValue();
				fichier.write(l.get(0).toString());
				for(int i = 1; i < l.size(); i++) {
					fichier.write(",,," + l.get(i));
				}
				tailleOccupee -= l.size();
			} else {
				fichier.write(e.getKey() + ";;;" + e.getValue() + "\n");
				tailleOccupee--;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void chargerObjet(String id) {
		try (
				BufferedReader in = new BufferedReader(new FileReader("object_list.txt"));
				FileWriter out = new FileWriter("object_list.txt");
				) {
			String line;
			while((line = in.readLine()) != null) {
				String[] parts = line.split(";;;");
				if (id.equals(parts[0])) {
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
