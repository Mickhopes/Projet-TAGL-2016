package fr.uga.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe qui définit le protocole du client
 */
public class ChatClientServeur implements Runnable {
	
	/**
	 * Socket pour la communication client-serveur
	 */
	private Socket socket;
	
	/**
	 * Contructeur
	 * 
	 * @param s la socket associee a la connexion etablie
	 */
	public ChatClientServeur(Socket s){
		socket = s;
	}
	
	/**
	 * Implementation de la methode run de l'interface Runnable
	 * 
	 * Tourne en boucle en envoyant les requetes saisies par le client
	 */
	public void run() {
		try {
			ObjectOutputStream out2 = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in2 = new ObjectInputStream(socket.getInputStream());
			
			Scanner sc = new Scanner(System.in);
			String requete;
			
			String message = (String) in2.readObject();
			if (message.equals("ready")) {
				System.out.println("le serveur est pret a recevoir une requete.");
				
				//Affichage de l'aide
				requete = "HELP";
				out2.writeObject(requete);
				out2.flush();
				message = (String) in2.readObject();
				System.out.println(message);

				requete = "vide";
				while (!requete.equals("deconnexion")) {
					
					//Commande utilisateur
					System.out.print("Entrez une commande :");
					requete = sc.nextLine();
					out2.writeObject(requete);
					out2.flush();
					message = (String) in2.readObject();
					System.out.println(message);
				}

			} else {
				System.out.println("le serveur n'est pas pret."+ message);
			}
			
			//Fin normale du client
			System.out.println("Le client se deconnecte");
			socket.close();
			
		    
		} catch (IOException e) {
			System.out.println("Deconnexion effectue");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
