package it.isislab.p2p.SemanticSocialNetwork;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import it.isislab.p2p.SemanticSocialNetwork.MessageListener;
import it.isislab.p2p.SemanticSocialNetwork.SemanticHarmonySocialNetworkImpl;

public class JUnitTest {
	
	class MessageListenerImpl implements MessageListener{
		int peerid;
		public MessageListenerImpl(int peerid)
		{
			this.peerid=peerid;
		}
		public Object parseMessage(Object obj) {
			System.out.println(peerid+"] (Direct Message Received) "+obj);
			return "success";
		}
	}
	
	private SemanticHarmonySocialNetworkImpl peer0;
	private SemanticHarmonySocialNetworkImpl peer1;
	private SemanticHarmonySocialNetworkImpl peer2;
	private SemanticHarmonySocialNetworkImpl peer3;
	private SemanticHarmonySocialNetworkImpl peer4;
	
	private ArrayList<String> questions;
	
	public JUnitTest() throws IOException {
		
		peer0 = new SemanticHarmonySocialNetworkImpl(0, "127.0.0.1", new MessageListenerImpl(0));
		peer1 = new SemanticHarmonySocialNetworkImpl(1, "127.0.0.1", new MessageListenerImpl(1));
		peer2 = new SemanticHarmonySocialNetworkImpl(2, "127.0.0.1", new MessageListenerImpl(2));
		peer3 = new SemanticHarmonySocialNetworkImpl(3, "127.0.0.1", new MessageListenerImpl(3));
		peer4 = new SemanticHarmonySocialNetworkImpl(4, "127.0.0.1", new MessageListenerImpl(4));
		
	}
	
	@Test
	public void testSemanticHarmonySocialNetworkMethod() {
		
		//creo la lista di domande
		ArrayList<String> questions=new ArrayList<String>();
		questions.add("Ti piace il mare?");
		questions.add("Ti piace la montagna?");
		questions.add("Ti piace il cinema?");
		questions.add("Ti piace lo sport?");
		questions.add("Ti piace fare shopping?");
		questions.add("Ti piace andare in discoteca?");
		questions.add("Ti piace la tecnologia?");
		questions.add("Ti piace viaggiare?");
		questions.add("Ti piace leggere?");
		
		//il primo peer aggiungela lista di domande alla rete
		
		assertEquals(true, peer0.putUserProfileQuestions(questions));
		
		//controllo se la lista delle domande aggiunte alla rete è uguale alla lista restituita da getUserProfileQuestions
		assertEquals(questions,(ArrayList<String>)peer0.getUserProfileQuestions());
		//creo la lista di risposte
		ArrayList<Integer> answers=new ArrayList<Integer>();
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		
		//creo la chiave del peer0 in base alle risposte
		String profile_key=peer0.createAuserProfileKey(answers);
		
		
		String profile_key_test="000000000";
		//controllo che la chiave generata da createAuserProfileKey sia corretta
		assertEquals(profile_key,profile_key_test);
		assertEquals(true,peer0.join(profile_key, "Pippo0"));
		
		//faccio gli stessi controlli per tutti i peer cambiando le risposte
		
		assertEquals(questions,(ArrayList<String>)peer1.getUserProfileQuestions());
		answers=new ArrayList<Integer>();
		answers.add(1);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		profile_key=peer1.createAuserProfileKey(answers);
		profile_key_test="100000000";
		assertEquals(profile_key,profile_key_test);
		assertEquals(true,peer1.join(profile_key, "Pippo1"));
		
		
		assertEquals(questions,(ArrayList<String>)peer2.getUserProfileQuestions());
		answers=new ArrayList<Integer>();
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		profile_key=peer2.createAuserProfileKey(answers);
		profile_key_test="111111111";
		assertEquals(profile_key,profile_key_test);
		assertEquals(true,peer2.join(profile_key, "Pippo2"));
		
		assertEquals(questions,(ArrayList<String>)peer3.getUserProfileQuestions());
		answers=new ArrayList<Integer>();
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		answers.add(0);
		profile_key=peer3.createAuserProfileKey(answers);
		profile_key_test="111000000";
		assertEquals(profile_key,profile_key_test);
		assertEquals(true,peer3.join(profile_key, "Pippo3"));
		
		
		assertEquals(questions,(ArrayList<String>)peer4.getUserProfileQuestions());
		answers=new ArrayList<Integer>();
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(0);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		answers.add(1);
		profile_key=peer4.createAuserProfileKey(answers);
		profile_key_test="111101111";
		assertEquals(profile_key,profile_key_test);
		assertEquals(true,peer4.join(profile_key, "Pippo4"));
		
		
		//stampo gli amici di ogni peer controllando che corrispondono a quelli previsti
		ArrayList<String> friends=new ArrayList<String>();
		
		ArrayList<String> friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo1");
		friends=(ArrayList<String>) peer0.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer0");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo0");
		friendsTest.add("Pippo3");
		friends=(ArrayList<String>) peer1.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer1");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo4");
		friends=(ArrayList<String>) peer2.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer2");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo1");
		friends=(ArrayList<String>) peer3.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer3");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo2");
		friends=(ArrayList<String>) peer4.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer4");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		
		assertEquals(true, peer0.leaveNetwork());
		System.out.println("peer0 è uscito dalla rete");
		
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo3");
		friends=(ArrayList<String>) peer1.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer1");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo4");
		friends=(ArrayList<String>) peer2.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer2");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo1");
		friends=(ArrayList<String>) peer3.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer3");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
		friendsTest=new ArrayList<String>();
		friendsTest.add("Pippo2");
		friends=(ArrayList<String>) peer4.getFriends();
		assertEquals(friends,friendsTest);
		System.out.println("amici di peer4");
		for(String friend:friends) {
			System.out.println(friend);
		}
		
	}
}
