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
			
			//Lire le mode tant que ce n'est pas "deconnection"
			//Le mode permet de de choisir entre l'ajout et la demande d'un objet.
			String mode = in.readLine();
			while (!mode.equals("deconnection")) {
				
				if (mode.equals("ajout")) {
					//Code qui va ajouter un objet sur disk / cache du serveur
					out.println("attente requete ajout");
					out.flush();
					String requeteAjout = in.readLine();
					//Lancement d'un thread qui va ajouter un objet sur le serveur.
					log.println(dateFormat.format(date)+"Serveur a un nouvel ajout : ["+requeteAjout+"]");
					
				}else if (mode.equals("demande")){
					//Code qui va executer la requete de demande et fournir une reponse
					out.println("attente requete demande");
					out.flush();
					String requeteToExecute = in.readLine();
					log.println(dateFormat.format(date)+"Serveur a une nouvelle requete a executer : ["+requeteToExecute+"]");
					requete = new Thread(new ExecutionRequete(requeteToExecute, out));
					requete.start();
				}else {
					out.println("Desole, je n'ai pas compris votre requete");
					out.flush();
				}
				
				mode = in.readLine();
			}
			
			//On a recu la deconnection normale du client
			fermetureSocket(socket);
			
		} catch (Exception e){
			System.out.println("(Serveur) Erreur ChatClientServeur");
			System.out.println("Le client s'est deconnecte, fermeture de la socket");
			log.println(dateFormat.format(date)+"Le client s'est deconnecte, fermeture de la socket");
			fermetureSocket(socket);
		}
	}

}
