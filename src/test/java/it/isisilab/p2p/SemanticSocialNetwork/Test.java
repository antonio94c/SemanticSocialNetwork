package it.isisilab.p2p.SemanticSocialNetwork;

import java.io.IOException;
import java.util.List;

import it.isisilab.p2p.SemanticSocialNetwork.*;

public class Test {
	
	public static void main(String[] args) throws IOException {
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
	
	SemanticHarmonySocialNetworkImpl peer0 = new SemanticHarmonySocialNetworkImpl(0,"127.0.0.1");
	SemanticHarmonySocialNetworkImpl peer1 = new SemanticHarmonySocialNetworkImpl(1,"127.0.0.1");
	SemanticHarmonySocialNetworkImpl peer2 = new SemanticHarmonySocialNetworkImpl(2,"127.0.0.1");
	
	String profileKey = "0001";	 
	String nickname = "antonio94c";
	String nickname1 = "giuseppe";
	String nickname2 = "ivan";
	
	peer0.join(profileKey, nickname);
	peer0.join(profileKey, nickname);
	peer0.join(profileKey, nickname);
	peer1.join(profileKey, nickname1);
		
	}
}
