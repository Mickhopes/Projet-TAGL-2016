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
	private ObjectInputStream in2;
	private ObjectOutputStream out2;
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
		ecritureLogs("Fermeture de la socket [" + socket.getInetAddress() + "]");
	}
	
	private void ecritureLogs(String chaine){
		try {
			date = new Date();
			log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			log.println(dateFormat.format(date)+chaine);
			log.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		try {
			//Ouverture du fichier de logs
			log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			in2 = new ObjectInputStream(socket.getInputStream());
			out2 = new ObjectOutputStream(socket.getOutputStream());
			
			//Envoie d'un message au client pour signifier que le serveur est pret a recevoir une requete.
			out2.writeObject("ready");
			out2.flush();

			//Lire la commande tant que ce n'est pas "deconnection"
			String commande = (String) in2.readObject();
			while (!commande.equals("deconnexion")) {
				
				//Lancement d'un thread qui va ajouter un objet sur le serveur.
				ecritureLogs("Serveur a une nouvelle commande : ["+commande+"]");
				requete = new Thread(new ExecutionRequete(commande, out2, stockage));
				requete.start();
				
				commande = (String) in2.readObject();
			}
			
			//On a recu la deconnexion normale du client
			fermetureSocket(socket);
			
		} catch (Exception e){
			System.out.println("(Serveur) Erreur ChatClientServeur");
			System.out.println("Le client s'est deconnecte, fermeture de la socket");
			ecritureLogs("Le client s'est deconnecte, fermeture de la socket");
			fermetureSocket(socket);
		}
	}

}
