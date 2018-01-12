package it.isisilab.p2p.SemanticSocialNetwork;

import java.io.IOException;
import java.util.ArrayList;


public class TestSemanticHarmonySocialNetworkImpl {

	public static void main(String[] args) {
		
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
		
		try {
			SemanticHarmonySocialNetworkImpl peer0 = new SemanticHarmonySocialNetworkImpl(0, "127.0.0.1", new MessageListenerImpl(0));
			
			SemanticHarmonySocialNetworkImpl peer1 = new SemanticHarmonySocialNetworkImpl(1, "127.0.0.1", new MessageListenerImpl(1));
			
			SemanticHarmonySocialNetworkImpl peer2 = new SemanticHarmonySocialNetworkImpl(2, "127.0.0.1", new MessageListenerImpl(2));
			
			SemanticHarmonySocialNetworkImpl peer3 = new SemanticHarmonySocialNetworkImpl(3, "127.0.0.1", new MessageListenerImpl(3));
			
			
			QuestionsInitImpl q=new QuestionsInitImpl();
			
			ArrayList<String> questions=q.initialize();
			
			peer0.putUserProfileQuestions(questions);
			
			ArrayList<Integer> answers=q.askQuestions((ArrayList<String>)peer0.getUserProfileQuestions());
			String profile_key=peer0.createAuserProfileKey(answers);
			peer0.join(profile_key, "Pippo0");
			
			answers=q.askQuestions((ArrayList<String>)peer1.getUserProfileQuestions());
			String profile_key1=peer1.createAuserProfileKey(answers);
			peer1.join(profile_key1, "Pippo1");
			
			answers=q.askQuestions((ArrayList<String>)peer2.getUserProfileQuestions());
			String profile_key2=peer2.createAuserProfileKey(answers);
			peer2.join(profile_key2, "Pippo2");
			
			answers=q.askQuestions((ArrayList<String>)peer3.getUserProfileQuestions());
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
			
			
			peer1.leaveNetwork();
			System.out.println("peer1 è uscito dalla rete");
			
			
			friends=(ArrayList<String>) peer0.getFriends();
			System.out.println("amici di peer0");
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
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
