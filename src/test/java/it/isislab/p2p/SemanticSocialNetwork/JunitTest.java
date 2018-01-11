package it.isislab.p2p.SemanticSocialNetwork;

import java.io.IOException;
import java.util.ArrayList;

import it.isislab.p2p.SemanticSocialNetwork.SemanticHarmonySocialNetworkImpl;
import junit.framework.TestCase;

public class JunitTest extends TestCase {
	
	public void test() {
		try {
			SemanticHarmonySocialNetworkImpl peer0 = new SemanticHarmonySocialNetworkImpl(0, "127.0.0.1");
			
			SemanticHarmonySocialNetworkImpl peer1 = new SemanticHarmonySocialNetworkImpl(1, "127.0.0.1");
			
			SemanticHarmonySocialNetworkImpl peer2 = new SemanticHarmonySocialNetworkImpl(2, "127.0.0.1");
			
			SemanticHarmonySocialNetworkImpl peer3 = new SemanticHarmonySocialNetworkImpl(3, "127.0.0.1");
			
			ArrayList<String> questions=new ArrayList<String>();
			questions=(ArrayList<String>) peer1.getUserProfileQuestions();
			
			for(String question:questions) {
				System.out.println(question);
			}
			
			
			//ogni peer risponde alle domande, crea la chiave e aggiunge gli amici
			ArrayList<Integer> answers=new ArrayList<Integer>();
			answers.add(0);
			answers.add(0);
			answers.add(0);
			answers.add(0);
			String profile_key=peer0.createAuserProfileKey(answers);
			peer0.join(profile_key, "Pippo0");
			
			String profile_key_test="0000";
			assertEquals(profile_key,profile_key_test);
			
			answers=new ArrayList<Integer>();
			answers.add(1);
			answers.add(0);
			answers.add(1);
			answers.add(0);
			String profile_key1=peer1.createAuserProfileKey(answers);
			peer1.join(profile_key1, "Pippo1");
			
			String profile_key_test1="1010";
			assertEquals(profile_key1,profile_key_test1);
			
			answers=new ArrayList<Integer>();
			answers.add(1);
			answers.add(0);
			answers.add(1);
			answers.add(1);
			String profile_key2=peer2.createAuserProfileKey(answers);
			peer2.join(profile_key2, "Pippo2");
			
			String profile_key_test2="1011";
			assertEquals(profile_key2,profile_key_test2);
			
			answers=new ArrayList<Integer>();
			answers.add(0);
			answers.add(1);
			answers.add(0);
			answers.add(1);
			String profile_key3=peer3.createAuserProfileKey(answers);
			peer3.join(profile_key3, "Pippo3");
			
			String profile_key_test3="0101";
			assertEquals(profile_key3,profile_key_test3);
			
			
			//stampo gli amici di ogni peer
			ArrayList<String> friends=new ArrayList<String>();
			
			ArrayList<String> friendsTest=new ArrayList<String>();
			friendsTest.add("Pippo0");
			friends=(ArrayList<String>) peer0.getFriends();
			assertEquals(friends,friendsTest);
			System.out.println("amici di peer0");
			for(String friend:friends) {
				System.out.println(friend);
			}
			
			friendsTest=new ArrayList<String>();
			friendsTest.add("Pippo1");
			friendsTest.add("Pippo3");
			friends=(ArrayList<String>) peer1.getFriends();
			assertEquals(friends,friendsTest);
			System.out.println("amici di peer1");
			for(String friend:friends) {
				System.out.println(friend);
			}
			
			friendsTest=new ArrayList<String>();
			friendsTest.add("Pippo2");
			friends=(ArrayList<String>) peer2.getFriends();
			assertEquals(friends,friendsTest);
			System.out.println("amici di peer2");
			for(String friend:friends) {
				System.out.println(friend);
			}
			
			friendsTest=new ArrayList<String>();
			friendsTest.add("Pippo1");
			friendsTest.add("Pippo3");
			friends=(ArrayList<String>) peer3.getFriends();
			assertEquals(friends,friendsTest);
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
