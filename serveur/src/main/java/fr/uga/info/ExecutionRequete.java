package fr.uga.info;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Classe qui gere l'execution des requetes reçues par le serveur
 */
public class ExecutionRequete implements Runnable {
	/**
	 * la commande a traiter (envoyee par le client)
	 */
	private String commande;
	/**
	 * l'ObjectOutputStream sur lequel la reponse sera envoyee
	 */
	private ObjectOutputStream out2;
	/**
	 * Objet Stockage associe au serveur
	 */
	private Stockage stockage;
	
	/**
	 * Contructeur
	 * 
	 * @param requete la requete a traitée
	 * @param o l'ObjectOutputStream sur lequel la reponse sera envoyee
	 * @param stk l'objet Stockage associe au serveur
	 */
	public ExecutionRequete (String requete, ObjectOutputStream o, Stockage stk) {
		commande = requete;
		out2 = o;
		stockage = stk;
	}

	/**
	 * Implementation de la methode run de l'interface Runnable
	 * 
	 * Traite la requete du client et envoie la reponse correspondante
	 */
	public void run() {
		try {
			
			String[] args = commande.split(" ");
			switch(args[0]){
			case "HELP":
				out2.writeObject("Liste des commandes accesptees :\nSET <Objet> <Valeur>\nGET <Objet>\nDEL <Objet>\nRPUSH <Objet> <Valeur>\nLPUSH <Objet> <Valeur>\nRPOP <Objet>\nLPOP <Object>\nLRANGE <Objet> <Indice debut> <Indice fin>");
				out2.flush();
				break;
			case "SET":
				if (args.length != 3) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					stockage.ajouterObjet(args[1], (Object) args[2]); 
					out2.writeObject("ok");
					out2.flush();
				}
				break;
			case "GET":
				if (args.length != 2) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					Object result = stockage.recupererObjet(args[1]);
					if (result == null) {
						out2.writeObject("L'objet ["+args[1]+"] n'existe pas");
						out2.flush();
					} else {
						out2.writeObject((String) result);
						out2.flush();
					}
				}
				break;
			case "DEL":
				if (args.length != 2) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					boolean result = stockage.supprimerObjet(args[1]);
					if (!result) {
						out2.writeObject("L'objet ["+args[1]+"] n'existe pas");
						out2.flush();
					} else {
						out2.writeObject(args[1]+" a bien ete supprime");
						out2.flush();
					}
				}
				break;
			case "RPUSH":
				if (args.length != 3) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					int result = stockage.ajoutListeObject(args[1], (Object) args[2], true);
					if (result == -1) {
						out2.writeObject(args[1]+" n'est pas une liste d'Objet");
						out2.flush();
					}else {
						out2.writeObject(String.valueOf(result));
						out2.flush();
					}
				}
				break;
			case "LPUSH":
				if (args.length != 3) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					int result = stockage.ajoutListeObject(args[1], (Object) args[2], false);
					if (result == -1) {
						out2.writeObject(args[1]+" n'est pas une liste d'Objet");
						out2.flush();
					}else {
						out2.writeObject(String.valueOf(result));
						out2.flush();
					}
				}
				break;
			case "RPOP":
				if (args.length != 2) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					Object result = stockage.supprimerListeObject(args[1], false);
					if (result == null) {
						out2.writeObject(args[1]+" n'est pas une liste d'Objet");
						out2.flush();
					}else {
						out2.writeObject(result);
						out2.flush();
					}
				}
				break;
			case "LPOP":
				if (args.length != 2) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					Object result = stockage.supprimerListeObject(args[1], true);
					if (result == null) {
						out2.writeObject(args[1]+" n'est pas une liste d'Objet");
						out2.flush();
					}else {
						out2.writeObject(result);
						out2.flush();
					}
				}
				break;
			case "LRANGE":
				if (args.length != 4) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					List<Object> result = stockage.recupererListeObject(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
					if (result == null) {
						out2.writeObject("RANGE incorrecte ou la liste n'existe pas");
						out2.flush();
					}else {
						String retour = "";
						for (int i = 0; i < result.size(); i++) {
							retour += i+1 +") \"" +(String)result.get(i)+"\"\n";
						}
						out2.writeObject(retour);
						out2.flush();
					}
				}
				break;
			default :
				out2.writeObject("Commande invalide");
				out2.flush();
				break;
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
