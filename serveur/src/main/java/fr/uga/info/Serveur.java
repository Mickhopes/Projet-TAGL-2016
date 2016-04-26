package fr.uga.info;

import java.io.IOException;
import java.net.ServerSocket;

public class Serveur {
	
	public static ServerSocket ss = null;
	public static Thread t;
	
	public static void main(String[] args) {
		try {
			ss = new ServerSocket(4242);
		} catch (IOException e) {
			System.out.println("Erreur dans la creation de la socket (Serveur)");
		}
		
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
		
		t = new Thread(new AccepterConnexion(ss));
		t.start();
	}
}
