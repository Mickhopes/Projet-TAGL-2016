package fr.uga.info;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientServeur implements Runnable{

	
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private Thread requete;
	private PrintWriter log;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("[dd/MM HH:mm:ss]");

	
	public ChatClientServeur(Socket s){
		socket = s;
	}
	
	
	public void run() {
		try {
			
			//Ouverture du fichier de logs
			log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			//Creation du canal de lecture depuis le client
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//Creation du canal d'ecriture vers le client
			out = new PrintWriter(socket.getOutputStream());
			
			//Lire la requete
			String requeteToExecute = in.readLine();
			log.println("Serveur a une nouvelle requete a executer : ["+requeteToExecute+"]");
			log.close();
			
			requete = new Thread(new ExecutionRequete(requeteToExecute));
			requete.start();
			
		} catch (Exception e){
			System.out.println("Erreur ChatClientServeur");
		}
	}

}
