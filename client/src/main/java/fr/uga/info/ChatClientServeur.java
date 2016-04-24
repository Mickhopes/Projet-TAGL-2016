package fr.uga.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientServeur implements Runnable {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private Scanner sc;
	
	public ChatClientServeur(Socket s){
		socket = s;
	}
	
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			sc = new Scanner(System.in);
			
			/*Thread t4 = new Thread(new Emission(out));
			t4.start();
			Thread t3 = new Thread(new Reception(in2,in));
			t3.start();*/
			String message = in.readLine();
			if (message.equals("ready")) {
				System.out.println("le serveur est pret a recevoir une requete.");
				out.println("Requete test");
				out.flush();
			}else {
				System.out.println("le serveur n'est pas pret."+ message);
			}
			
			//Attente de la reponse
			String reponse = in.readLine();
			System.out.println("Reponse du serveur :"+ reponse);
			
			//Fin normale du client
			System.out.println("Le client se deconnecte");
			out.println("deconnection");
			out.flush();
			socket.close();
			
		    
		} catch (IOException e) {
			System.err.println("Le serveur distant s'est déconnecté !");
		}
	}

}
