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
	//private PrintWriter out = null;
	//private BufferedReader in = null;
	private ObjectInputStream in2;
	private ObjectOutputStream out2;
	private Scanner sc;
	
	public ChatClientServeur(Socket s){
		socket = s;
	}
	
	public void run() {
		try {
			//out = new PrintWriter(socket.getOutputStream());
			out2 = new ObjectOutputStream(socket.getOutputStream());
			//in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in2 = new ObjectInputStream(socket.getInputStream());
			
			sc = new Scanner(System.in);
			String requete;
			
			/*Thread t4 = new Thread(new Emission(out));
			t4.start();*/
			
			//String message = in.readLine();
			String message = (String) in2.readObject();
			if (message.equals("ready")) {
				System.out.println("le serveur est pret a recevoir une requete.");
				
				//Affichage de l'aide
				requete = "HELP";
				out2.writeObject(requete);
				out2.flush();
				message = (String) in2.readObject();
				System.out.println(message);

				//Commande utilisateur
				System.out.print("Entrez une commande :");
				requete = sc.nextLine();
				out2.writeObject(requete);
				out2.flush();				

			}else {
				System.out.println("le serveur n'est pas pret."+ message);
			}
			
			//Attente de la reponse
			String reponse = (String) in2.readObject();
			//String reponse = in.readLine();
			System.out.println("Reponse du serveur :"+ reponse);
			
			//Fin normale du client
			System.out.println("Le client se deconnecte");
			out2.writeObject("deconnection");
			out2.flush();
			socket.close();
			
		    
		} catch (IOException e) {
			System.err.println("Le serveur distant s'est deconnecte !");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
