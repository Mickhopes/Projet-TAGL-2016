package fr.uga.info;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class ExecutionRequete implements Runnable {
	private Object requeteToExecute;
	//private PrintWriter out;
	private ObjectOutputStream out2;
	private boolean demande;
	private Stockage stockage;
	
	public ExecutionRequete (Object requete, ObjectOutputStream o, boolean demde, Stockage stk) {
		requeteToExecute = requete;
		//out = o;
		out2 = o;
		demande = demde;
		stockage = stk;
	}

	public void run() {
		try {
			if(demande){
				System.out.println("Execution demande : "+requeteToExecute.toString());
				out2.writeObject("reponse de ExcutionRequete");
				out2.flush();
			//Ajout
			}else if (!demande){
				System.out.println("Execution ajout : "+requeteToExecute.toString());
				out2.writeObject("ajout object ok");
				out2.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//out.println("reponse de ExecutionRequete");
		//out.flush();
	}
}
