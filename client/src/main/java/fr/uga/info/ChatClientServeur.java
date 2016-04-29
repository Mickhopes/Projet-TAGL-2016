package fr.uga.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientServeur implements Runnable {

	private Socket socket;
	private ObjectInputStream in2;
	private ObjectOutputStream out2;
	private Scanner sc;
	
	public ChatClientServeur(Socket s){
		socket = s;
	}
	
	public void run() {
		try {
			out2 = new ObjectOutputStream(socket.getOutputStream());
			in2 = new ObjectInputStream(socket.getInputStream());
			
			sc = new Scanner(System.in);
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

			}else {
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
