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

public class ChatClientServeur implements Runnable{

	
	private Socket socket = null;
	//private BufferedReader in = null;
	private ObjectInputStream in2;
	private ObjectOutputStream out2;
	//private PrintWriter out = null;
	private Thread requete;
	private PrintWriter log;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("[dd/MM HH:mm:ss]");
	private Stockage stockage;

	
	public ChatClientServeur(Socket s, Stockage stk){
		socket = s;
		stockage = stk;
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
			//in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in2 = new ObjectInputStream(socket.getInputStream());
			//Creation du canal d'ecriture vers le client
			//out = new PrintWriter(socket.getOutputStream());
			out2 = new ObjectOutputStream(socket.getOutputStream());
			
			//Envoie d'un message au client pour signifier que le serveur est pret a recevoir une requete.
			out2.writeObject("ready");
			out2.flush();
			//out.println("ready");
			//out.flush();
			
			//Lire le mode tant que ce n'est pas "deconnection"
			//Le mode permet de de choisir entre l'ajout et la demande d'un objet.
			String mode = (String) in2.readObject();
			//String mode = in.readLine();
			while (!mode.equals("deconnection")) {
				
				if (mode.equals("ajout")) {
					//Code qui va ajouter un objet sur disk / cache du serveur
					out2.writeObject("attente requete ajout");
					out2.flush();
					//out.println("attente requete ajout");
					//out.flush();
					Object requeteAjout = in2.readObject();
					//String requeteAjout = in.readLine();
					//Lancement d'un thread qui va ajouter un objet sur le serveur.
					log.println(dateFormat.format(date)+"Serveur a un nouvel ajout : ["+requeteAjout+"]");
					requete = new Thread(new ExecutionRequete(requeteAjout, out2, false, stockage));
					requete.start();
					
				}else if (mode.equals("demande")){
					//Code qui va executer la requete de demande et fournir une reponse
					out2.writeObject("attente requete demande");
					out2.flush();
					//out.println("attente requete demande");
					//out.flush();
					Object requeteToExecute = in2.readObject();
					//String requeteToExecute = in.readLine();
					log.println(dateFormat.format(date)+"Serveur a une nouvelle requete a executer : ["+requeteToExecute+"]");
					requete = new Thread(new ExecutionRequete(requeteToExecute, out2, true, stockage));
					requete.start();
					
				}else {
					out2.writeObject("Desole, je n'ai pas compris votre requete");
					out2.flush();
					//out.println("Desole, je n'ai pas compris votre requete");
					//out.flush();
				}
				
				mode = (String) in2.readObject();
				//mode = in.readLine();
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
