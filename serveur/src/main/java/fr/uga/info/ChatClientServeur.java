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
			in2 = new ObjectInputStream(socket.getInputStream());
			out2 = new ObjectOutputStream(socket.getOutputStream());
			
			//Envoie d'un message au client pour signifier que le serveur est pret a recevoir une requete.
			out2.writeObject("ready");
			out2.flush();

			//Lire la commande tant que ce n'est pas "deconnection"
			String commande = (String) in2.readObject();
			while (!commande.equals("deconnection")) {
				
				//Lancement d'un thread qui va ajouter un objet sur le serveur.
				log.println(dateFormat.format(date)+"Serveur a une nouvelle commande : ["+commande+"]");
				requete = new Thread(new ExecutionRequete(commande, out2, stockage));
				requete.start();
				
				commande = (String) in2.readObject();
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
