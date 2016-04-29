package fr.uga.info;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	static final int TAILLE_LIMITE = 2000;
	
	/**
	 * Taille limite d'objets dans une liste.
	 */
	static final int TAILLE_LIMITE_LISTE = 200;
	
	/**
	 * Attribut contenant le nombre d'objets en mémoire.
	 */
	private int tailleOccupee;
	
	/**
	 * Attribut contenant la taille limite d'objets en mémoire.
	 */
	private int tailleLimite;
	
	/**
	 * Attribut contenant la taille limite d'objets dans une liste en mémoire.
	 */
	private int tailleLimiteListe;
	
	/**
	 * Map servant à associer un id à un objet.
	 */
	private HashMap<String, Object> map;
	
	/**
	 * Contructeur avec définition de taille limite.
	 * 
	 * @param tailleLimite Taille limite d'objets en mémoire.
	 * @param tailleLimiteList Taille limite d'objets dans une liste en mémoire.
	 */
	public Stockage(int tailleLimite, int tailleLimiteListe) {
		map = new HashMap<>();
		tailleOccupee = 0;
		this.tailleLimite = tailleLimite;
		this.tailleLimiteListe = tailleLimiteListe;
	}
	
	/**
	 * Contructeur avec définition de taille limite.
	 * 
	 * @param tailleLimite Taille limite d'objets en mémoire.
	 */
	public Stockage(int tailleLimite) {
		this(tailleLimite, TAILLE_LIMITE_LISTE);
	}
	
	/**
	 * Constructeur par défaut.
	 * Met la taille limite d'objets à celle définit par TAILLE_LIMITE.
	 */
	public Stockage() {
		this(TAILLE_LIMITE, TAILLE_LIMITE_LISTE);
	}
	
	/**
	 * Ajoute un objet en mémoire associé à un id.
	 * 
	 * @param id Id de l'objet à mettre en mémoire.
	 * @param o L'objet à mettre en mémoire.
	 */
	public synchronized void ajouterObjet(String id, Object o) {
		if (tailleOccupee == tailleLimite) {
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
		if (map.containsKey(id)) {
			if (map.get(id) instanceof LinkedList<?>) {
				tailleOccupee -= ((LinkedList<Object>)map.get(id)).size();
			}
			
			map.remove(id);
			supprimerFichier(id);
			return true;
		} else {
			return supprimerFichier(id);
		}
	}
	
	/**
	 * Ajoute l'object dans la liste renseignée par "id".
	 * Si la liste n'existe pas, elle est crée.
	 * 
	 * @param id L'id de la liste.
	 * @param o L'object à ajouter.
	 * @param fin true pour ajouter à la fin de la liste, false au début.
	 * @return -1 si l'object renseigné par "id" n'est pas une liste, -2 si la taille limite de la liste est atteinte sinon la nouvelle taille de la liste.
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
			return listeObjet.size();
		} else {
			// On vérifie d'abords que l'objet est bien une liste
			if (liste instanceof LinkedList<?>) {
				LinkedList<Object> listeObjet = (LinkedList<Object>)liste;
				if (listeObjet.size() == tailleLimiteListe) {
					return -2;
				}
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
	 * @return le nombre d'objets stocké en mémoire.
	 */
	public synchronized int nombreObjet() {
		return map.size();
	}
	
	/**
	 * Renvoi le nombre d'objet de la liste stocké en mémoire.
	 * 
	 * @param id L'id de la liste.
	 * @return Le nombre d'object de la liste, 0 si la liste n'existe pas, -2 si l'objet n'est pas une liste.
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
		return 0;
	}
	
	private void sauvegarderPlusAncien() {
		Entry<String, Object> e = map.entrySet().iterator().next();
		new File("Objets").mkdirs();
		try (
				FileOutputStream fichier = new FileOutputStream(new File("./Objets/" + e.getKey() + ".ser"));
				ObjectOutputStream oos = new ObjectOutputStream(fichier);
				) {
			oos.writeObject(e.getValue());
			oos.flush();
			
			if (e.getValue() instanceof LinkedList<?>) {
				tailleOccupee -= ((LinkedList<Object>)e.getValue()).size();
			} else {
				tailleOccupee--;
			}
			
			System.out.println("fichier \"" + e.getKey() + ".ser\" créé");
			
			map.remove(e.getKey());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void chargerObjet(String id) {
		try (
				FileInputStream fichier = new FileInputStream(new File("./Objets/" + id + ".ser"));
				ObjectInputStream ois = new ObjectInputStream(fichier);
				) {
			Object o = ois.readObject();
			
			if (o instanceof LinkedList<?>) {
				LinkedList<Object> l = (LinkedList<Object>)o;
				
				while(tailleOccupee+l.size() >= tailleLimite) {
					sauvegarderPlusAncien();
				}
				
				map.put(id, l);
				tailleOccupee += l.size();
			} else {
				if (tailleOccupee == tailleLimite) {
					sauvegarderPlusAncien();
				}
				
				map.put(id, o);
				tailleOccupee++;
			}
		} catch(FileNotFoundException ex) {
			System.out.println("Chargement de \"" + id + ".ser\" impossible, l'objet n'existe pas");
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean supprimerFichier(String id) {
		File suppr = new File("./Objets/" + id + ".ser");
		suppr.getAbsoluteFile().delete();
		return true;
	}
}
