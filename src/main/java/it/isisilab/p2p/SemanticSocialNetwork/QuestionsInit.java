package it.isisilab.p2p.SemanticSocialNetwork;

import java.util.ArrayList;

public interface QuestionsInit {

	// Metodo invocato dal primo peer che accede alla rete ed inizializza il questionario
	public ArrayList<String> initialize();
	
	// Metodo che ottiene le risposte alle domande del questionario
	// invocato su tutti i peer che si collegano alla rete
	public ArrayList<Integer> askQuestions(ArrayList<String> str);

	// Stampa in console il riepilogo domande/risposte
	public void echoAnswers();
}