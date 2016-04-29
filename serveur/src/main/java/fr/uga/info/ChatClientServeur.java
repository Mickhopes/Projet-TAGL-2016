package fr.uga.info;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe qui définit le protocole du serveur
 */
public class ChatClientServeur implements Runnable{
	
	/**
	 * Socket pour la communication client-serveur
	 */
	private Socket socket = null;
	/**
	 * Objet Stockage, recupere du serveur via AccepterConnexion
	 */
	private Stockage stockage;
	/**
	 * format de Date a utiliser dans le log
	 */
	private final DateFormat dateFormat = new SimpleDateFormat("[dd/MM HH:mm:ss]");
	
	/**
	 * Contructeur
	 * 
	 * @param s la socket associee a la connexion etablie
	 * @param stk l'objet Stockage associe au serveur
	 */
	public ChatClientServeur(Socket s, Stockage stk){
		socket = s;
		stockage = stk;
	}
	
	/**
	 * Ecrit la chaine passee en parametre dans le fichier de log
	 * 
	 * @param chaine la chaine a ecrire
	 */
	private void ecritureLogs(String chaine){
		try {
			Date date = new Date();
			PrintWriter log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			log.println(dateFormat.format(date)+chaine);
			log.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * appelle ecritureLogs avec un message de fermeture
	 * 
	 * @param s la socket (son adresse inet est requise)
	 */
	private void fermetureSocket(Socket s){
		ecritureLogs("Fermeture de la socket [" + socket.getInetAddress() + "]");
	}
	
	/**
	 * Implementation de la methode run de l'interface Runnable
	 * 
	 * Tourne en boucle et execute les requetes du client
	 * Chaque requete entraine la creation d'un thread
	 * qui va appeler la classe ExecutionRequete
	 */
	public void run() {
		try {
			//Ouverture du fichier de logs
			PrintWriter log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			ObjectInputStream in2 = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out2 = new ObjectOutputStream(socket.getOutputStream());
			
			//Envoie d'un message au client pour signifier que le serveur est pret a recevoir une requete.
			out2.writeObject("ready");
			out2.flush();

			//Lire la commande tant que ce n'est pas "deconnection"
			String commande = (String) in2.readObject();
			while (!commande.equals("deconnexion")) {
				
				//Lancement d'un thread qui va ajouter un objet sur le serveur.
				ecritureLogs("Serveur a une nouvelle commande : ["+commande+"]");
				Thread requete = new Thread(new ExecutionRequete(commande, out2, stockage));
				requete.start();
				
				commande = (String) in2.readObject();
			}
			
			//On a recu la deconnexion normale du client
			fermetureSocket(socket);
			
		} catch (Exception e){
			System.out.println("(Serveur) Erreur ChatClientServeur");
			System.out.println("Le client s'est deconnecte, fermeture de la socket");
			ecritureLogs("Le client s'est deconnecte, fermeture de la socket");
			fermetureSocket(socket);
		}
	}

}
