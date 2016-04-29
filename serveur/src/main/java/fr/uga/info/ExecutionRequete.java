package fr.uga.info;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class ExecutionRequete implements Runnable {
	private String commande;
	//private PrintWriter out;
	private ObjectOutputStream out2;
	private Stockage stockage;
	
	public ExecutionRequete (String requete, ObjectOutputStream o, Stockage stk) {
		commande = requete;
		out2 = o;
		stockage = stk;
	}

	public void run() {
		try {
			
			String[] args = commande.split(" ");
			switch(args[0]){
			case "HELP":
				out2.writeObject("Liste des commandes accesptees :\nSET <Objet> <Valeur>\nGET <Objet>");
				out2.flush();
				break;
			case "SET":
				if (args.length != 3) {
					out2.writeObject("Nombre d'argument incorrect");
					out2.flush();
				} else {
					
				}
			default :
				break;
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			/*if(demande){
				System.out.println("Execution demande : "+requeteToExecute.toString());
				out2.writeObject("reponse de ExcutionRequete");
				out2.flush();
			//Ajout
			}else if (!demande){
				System.out.println("Execution ajout : "+requeteToExecute.toString());
				out2.writeObject("ajout object ok");
				out2.flush();
			}*/
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//out.println("reponse de ExecutionRequete");
		//out.flush();
	}
}
