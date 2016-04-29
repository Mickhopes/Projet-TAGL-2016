package fr.uga.info;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static Socket socket = null;
	public static Thread t1;
	final static int port = 4242;
	
	public static void main(String[] args) {

		try {
			
			System.out.println("Demande de connexion");
			socket = new Socket("127.0.0.1", port);
			System.out.println("Connexion etablie avec le serveur"); // Si le message s'affiche c'est que je suis connect�
			
			t1 = new Thread(new ChatClientServeur(socket));
			t1.start();
		
		} catch (UnknownHostException e) {
		  System.err.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
		} catch (IOException e) {
		  System.err.println("Aucun serveur à l'ecoute du port " + port);
		}

	}

}
