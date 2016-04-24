package fr.uga.info;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.PasswordAuthentication;
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
	
	private void fermetureSocket(Socket s){
		try {
			log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			log.println("Fermeture de la socket [" + socket.getInetAddress() + "]");
			s.close();
			log.close();
		} catch (IOException e) {
			System.out.println("(Serveur) Erreur fermeture socket");
		}
	}
	
	
	public void run() {
		try {
			
			//Ouverture du fichier de logs
			log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			//Creation du canal de lecture depuis le client
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//Creation du canal d'ecriture vers le client
			out = new PrintWriter(socket.getOutputStream());
			
			//Envoie d'un message au client pour signifier que le serveur est pret a recevoir une requete.
			out.println("ready");
			out.flush();
			//Lire une requete tant que le message n'est pas "deconnection"
			String requeteToExecute = in.readLine();
			while (!requeteToExecute.equals("deconnection")) {
				log.println("Serveur a une nouvelle requete a executer : ["+requeteToExecute+"]");
				log.close();
				
				requete = new Thread(new ExecutionRequete(requeteToExecute, out));
				requete.start();
				
				requeteToExecute = in.readLine();
			}
			
			//On a recu la deconnection normale du client
			fermetureSocket(socket);
			
		} catch (Exception e){
			System.out.println("(Serveur) Erreur ChatClientServeur");
			System.out.println("Le client s'est déconnecté, fermeture de la socket");
			log.println("Le client s'est deconnecte, fermeture de la socket");
			log.close();
			fermetureSocket(socket);
		}
	}

}
