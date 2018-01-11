package it.isisilab.p2p.SemanticSocialNetwork;

import java.util.ArrayList;
import java.util.Scanner;

public class QuestionsInitImpl implements QuestionsInit{

	public ArrayList<String> questions;
	public ArrayList<Integer> answers;
	
	//classe 
	public QuestionsInitImpl() {}
	
	public ArrayList<String> initialize() {
		questions = new ArrayList<String>();
		answers = new ArrayList<Integer>();
		
		System.out.println("*************************************************************");
		System.out.println("******* Benvenuto in Semantic Harmony Social Network*********");
		System.out.println("******* sei il primo Peer a collegarti alla rete ***********");
		System.out.println("*************************************************************");
		System.out.println("Fornisci una serie di domande per creare la tua rete di amicizie");
		
		Scanner readData;
		String str_q;
		while(true) {
			System.out.println("Inserici la domanda numero "+(questions.size()+1)+", stop per interrompere");
			readData = new Scanner(System.in);  
			str_q = readData.nextLine();
			if(str_q.equals("")) {
				System.out.println("Non puoi inserire una domanda vuota. Riprova.");
			}else if(str_q.equals("stop")){
				
				if(questions.size() < 3) {
					System.out.println("Devi inserire almeno 3 domande!");
					continue;
				}
				System.out.println("Il Questionario è stato inizializzato correttamente!");
				break;
			}else {
				questions.add(str_q);
			}
		}
		
		return questions;
	}
	
	
	public ArrayList<Integer> askQuestions(ArrayList<String> q) {
		
		System.out.println("******************************************************************");
		System.out.println("********************* Benvenuto Peer n. **************************");
		System.out.println("******************************************************************");
		
		answers = new ArrayList<Integer>();
		
		for(int i=0;i < q.size();i++) {
			Scanner readData = new Scanner(System.in);  
			System.out.println(q.get(i)+"(si/no)");
			String str = readData.nextLine();
			if(!str.equals("si") && !str.equals("no")) {
				System.out.println("La risposta non è valida. Le risposte consentite sono si/no.");
				i -= 1;
				continue;
			}else {
				if(str.equals("si"))
				answers.add(1);
				else
				answers.add(0);
			}
		}
		System.out.println("Il Questionario è concluso");
		this.echoAnswers();
		return answers;
	}
	
	public void echoAnswers() {
		System.out.println("di seguito le tue risposte:");
		for(int j = 0;j < questions.size();j++) {
			System.out.println(questions.get(j)+": "+(answers.get(j)==1?"si":"no"));
		}
		System.out.println("******************************************************************");
		System.out.println("******* In attesa che altri peer si connettano alla rete *********");
		System.out.println("******************************************************************");
		System.out.println("");
	}
}
