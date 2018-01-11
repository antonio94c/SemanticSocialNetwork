package it.isislab.p2p.SemanticSocialNetwork;

import java.io.IOException;
import java.util.ArrayList;

import it.isislab.p2p.SemanticSocialNetwork.SemanticHarmonySocialNetworkImpl;


public class TestSemanticHarmonySocialNetworkImpl {

	public static void main(String[] args) {
		
		try {
			SemanticHarmonySocialNetworkImpl peer0 = new SemanticHarmonySocialNetworkImpl(0, "127.0.0.1");
			
			SemanticHarmonySocialNetworkImpl peer1 = new SemanticHarmonySocialNetworkImpl(1, "127.0.0.1");
			
			SemanticHarmonySocialNetworkImpl peer2 = new SemanticHarmonySocialNetworkImpl(2, "127.0.0.1");
			
			SemanticHarmonySocialNetworkImpl peer3 = new SemanticHarmonySocialNetworkImpl(3, "127.0.0.1");
			
			ArrayList<String> questions=new ArrayList<String>();
			questions=(ArrayList<String>) peer1.getUserProfileQuestions();
			
			for(String question:questions) {
				System.out.println(question+"(si/no)");
			}
			
			
			//ogni peer risponde alle domande, crea la chiave e aggiunge gli amici
			ArrayList<Integer> answers=new ArrayList<Integer>();
			answers.add(0);
			answers.add(0);
			answers.add(0);
			answers.add(0);
			String profile_key=peer0.createAuserProfileKey(answers);
			peer0.join(profile_key, "Pippo0");
			
			answers=new ArrayList<Integer>();
			answers.add(1);
			answers.add(0);
			answers.add(1);
			answers.add(0);
			String profile_key1=peer1.createAuserProfileKey(answers);
			peer1.join(profile_key1, "Pippo1");
			
			answers=new ArrayList<Integer>();
			answers.add(1);
			answers.add(0);
			answers.add(1);
			answers.add(1);
			String profile_key2=peer2.createAuserProfileKey(answers);
			peer2.join(profile_key2, "Pippo2");
			
			answers=new ArrayList<Integer>();
			answers.add(0);
			answers.add(1);
			answers.add(0);
			answers.add(1);
			String profile_key3=peer3.createAuserProfileKey(answers);
			peer3.join(profile_key3, "Pippo3");
			
			
			//stampo gli amici di ogni peer
			ArrayList<String> friends=new ArrayList<String>();
			friends=(ArrayList<String>) peer0.getFriends();
			System.out.println("amici di peer0");
			for(String friend:friends) {
				System.out.println(friend);
			}
			friends=(ArrayList<String>) peer1.getFriends();
			System.out.println("amici di peer1");
			for(String friend:friends) {
				System.out.println(friend);
			}
			friends=(ArrayList<String>) peer2.getFriends();
			System.out.println("amici di peer2");
			for(String friend:friends) {
				System.out.println(friend);
			}
			friends=(ArrayList<String>) peer3.getFriends();
			System.out.println("amici di peer3");
			for(String friend:friends) {
				System.out.println(friend);
			}
			
			System.out.println("peer1 "+peer1.leaveNetwork());
			
			System.out.println("peer3 "+peer3.leaveNetwork());
			
			friends=(ArrayList<String>) peer3.getFriends();
			System.out.println("amici di peer3");
			for(String friend:friends) {
				System.out.println(friend);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
