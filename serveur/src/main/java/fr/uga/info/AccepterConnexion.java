package fr.uga.info;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe qui gere l'acceptation de la connexion d'un client au serveur
 */
public class AccepterConnexion implements Runnable {
	
	/**
	 * La server socket, recuperee du Serveur
	 */
	private ServerSocket socketserver = null;
	/**
	 * Objet Stockage, recupere du serveur et envoye a ChatClientServeur
	 */
	private Stockage stockage;
	/**
	 * format de Date a utiliser dans le log
	 */
	private final DateFormat dateFormat = new SimpleDateFormat("[dd/MM HH:mm:ss]");
	
	/**
	 * Contructeur
	 * 
	 * @param ss la socket associee au serveur
	 * @param stk l'objet Stockage associe au serveur
	 */
	public AccepterConnexion(ServerSocket ss, Stockage stk){
		socketserver = ss;
		stockage = stk;
	}
	
	/**
	 * Implementation de la methode run de l'interface Runnable
	 * 
	 * Tourne en boucle et accepte les demandes de connexion.
	 * Chaque demande acceptee entraine la creation d'un thread
	 * qui va appeler la classe ChatClientServeur (protocole serveur)
	 */
	public void run() {
		try {
			PrintWriter log;
			log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			log.println("Serveur ready !");
			log.close();
			while(true){
				log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
				Socket socket = socketserver.accept();
				System.out.println("Client "+socket.getInetAddress().getHostName()+" avec "+socket.getInetAddress().getHostAddress()+" s'est connecte");
				Date date = new Date();
				log.println(dateFormat.format(date)+"Client "+socket.getInetAddress().getHostName()+" avec "+socket.getInetAddress().getHostAddress()+" s'est connecte");
				log.close();
				
				Thread t1 = new Thread(new ChatClientServeur(socket, stockage));
				t1.start();
			}
		} catch (IOException e) {
			System.err.println("Erreur serveur");
		}
	}
}
