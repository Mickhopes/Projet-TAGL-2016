package fr.uga.info;

import java.io.PrintWriter;

public class ExecutionRequete implements Runnable {
	private String requeteToExecute;
	private PrintWriter out;
	
	public ExecutionRequete (String requete, PrintWriter o) {
		requeteToExecute = requete;
		out = o;
	}

	public void run() {
		out.println("reponse de ExecutionRequete");
		out.flush();
	}
}
