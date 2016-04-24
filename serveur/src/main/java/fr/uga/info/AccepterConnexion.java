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

public class AccepterConnexion implements Runnable{

	private ServerSocket socketserver = null;
	private Socket socket = null;
	public Thread t1, ps;
	private PrintWriter log;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("[dd/MM HH:mm:ss]");
	
	
	public AccepterConnexion(ServerSocket ss){
		socketserver = ss;
	}
	
	
	public void run() {
		try {
			log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			log.println("Serveur ready !");
			log.close();
			while(true){
				log =  new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
				socket = socketserver.accept();
				System.out.println("Client "+socket.getInetAddress().getHostName()+" avec "+socket.getInetAddress().getHostAddress()+" s'est connecté");
				date = new Date();
				log.println(dateFormat.format(date)+"Client "+socket.getInetAddress().getHostName()+" avec "+socket.getInetAddress().getHostAddress()+" s'est connecte");
				log.close();
				
				t1 = new Thread(new ChatClientServeur(socket));
				t1.start();
			}
		} catch (IOException e) {
			System.err.println("Erreur serveur");
		}
		
	}

}
