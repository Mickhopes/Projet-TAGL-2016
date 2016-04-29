package fr.uga.info;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Classe qui cree le serveur
 */
public class Serveur {
	
	/**
	 * La socket du serveur
	 */
	public static ServerSocket ss = null;
	
	/**
	 * methode main
	 * 
	 * @param args n'est pas utilise
	 */
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(4242);

			System.out.println("------------------------------------------------------");
			System.out.println("|                                                    |");
			System.out.println("|                                                    |");
			System.out.println("|                Projet TAGL - M1 Info               |");
			System.out.println("|                Aurelien Monnet-Paquet              |");
			System.out.println("|                Line Pouvaret                       |");
			System.out.println("|                Antoine Thebaud                     |");
			System.out.println("|                Mickael Turnel                      |");
			System.out.println("|                                                    |");
			System.out.println("|                                                    |");
			System.out.println("------------------------------------------------------");
			System.out.println("Le serveur est à l'ecoute du port "+ss.getLocalPort());
			
			Stockage stockage = new Stockage();
			
			Thread t = new Thread(new AccepterConnexion(ss, stockage));
			t.start();
		} catch (IOException e) {
			System.out.println("Erreur dans la creation de la socket (Serveur)");
		}
	}
}
