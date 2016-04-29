package fr.uga.info;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe qui cree un client
 */
public class Client {
	
	/**
	 * le port utilise pour l'etablissement de la connexion
	 */
	private final static int port = 4242;
	
	/**
	 * methode main
	 * 
	 * lance le client et appelle la classe ChatClientServeur (protocole client)
	 * @param args n'est pas utilise
	 */
	public static void main(String[] args) {
		Socket socket = null;
		try {			
			System.out.println("Demande de connexion");
			socket = new Socket("127.0.0.1", port);
			System.out.println("Connexion etablie avec le serveur"); // Si le message s'affiche c'est que je suis connect�
			
			Thread t1 = new Thread(new ChatClientServeur(socket));
			t1.start();		
		} catch (UnknownHostException e) {
		  System.err.println("Impossible de se connecter a l'adresse "+socket.getLocalAddress());
		} catch (IOException e) {
		  System.err.println("Aucun serveur a l'ecoute du port " + port);
		}

	}

}
