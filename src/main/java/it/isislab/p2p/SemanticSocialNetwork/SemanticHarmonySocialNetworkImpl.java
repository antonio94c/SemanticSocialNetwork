package it.isislab.p2p.SemanticSocialNetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

public class SemanticHarmonySocialNetworkImpl implements SemanticHarmonySocialNetwork{
	final private Peer peer;
	final private PeerDHT _dht;
	final private int DEFAULT_MASTER_PORT=4000;
	
	private String profileKey=null;
	private Peer_nick_address pna=null;
	private ArrayList<String> rooms=null;

	public SemanticHarmonySocialNetworkImpl( int _id, String _master_peer) throws IOException{
		rooms=new ArrayList<String>();
		
		 peer= new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT+_id).start();
		_dht = new PeerBuilderDHT(peer).start();	
		
		FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
		fb.awaitUninterruptibly();
		if(fb.isSuccess()) {
			peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}
	}
	
	/**
	 * Gets the social network users questions.
	 * @return a list of String that is the profile questions.
	 */
	public List<String> getUserProfileQuestions(){
		ArrayList<String> questions = new ArrayList<String>();
		questions.add("Ti piace il cinema?");
		questions.add("Ti piace lo sport?");
		questions.add("Ti piace il mare?");
		questions.add("Ti piace la montagna?");
		return questions;
	}
	
	/**
	 * Creates a new user profile key according the user answers.
	 * @param _answer a list of answers.
	 * @return a String, the obtained profile key.
	 */
	public String createAuserProfileKey(List<Integer> _answer) {
		String profileKey="";
		for(int i=0;i<_answer.size();i++) {
			profileKey=profileKey+_answer.get(i);
		}
		this.profileKey=profileKey;
		return profileKey;
	}
	
	/**
	 * Joins in the Network. An automatic messages to each potential new friend is generated.
	 * @param _profile_key a String, the user profile key according the user answers
	 * @param _nick_name a String, the nickname of the user in the network.
	 * @return true if the join success, fail otherwise.
	 */
	public boolean join(String _profile_key,String _nick_name) {
		try {
			rooms.add(_profile_key);
			
			String opposto="";
			for(int i=0;i<_profile_key.length();i++) {
				if(_profile_key.charAt(i)=='0')
					opposto=opposto+"1";
				else
					opposto=opposto+"0";
			}
			rooms.add(opposto);
			
			this.pna=new Peer_nick_address(_nick_name,_dht.peer().peerAddress());
			
			for(String room:rooms) {
				FutureGet futureGet = _dht.get(Number160.createHash(room)).start();
				futureGet.awaitUninterruptibly();
				if (futureGet.isSuccess()) {
					if(futureGet.isEmpty()) {
						_dht.put(Number160.createHash(room)).data(new Data(new HashSet<String>())).start().awaitUninterruptibly();
					}
					futureGet = _dht.get(Number160.createHash(room)).start();
					futureGet.awaitUninterruptibly();
					if (futureGet.isSuccess()) {
						HashSet<Peer_nick_address> peers_on_topic;
						peers_on_topic = (HashSet<Peer_nick_address>) futureGet.dataMap().values().iterator().next().object();
						peers_on_topic.add(pna);
						_dht.put(Number160.createHash(room)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
					}else {
						return false;
					}
				}else {
					return false;
				}
			}
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the nicknames of all automatically creates friendships. 
	 * @return a list of String.
	 */
	public List<String> getFriends(){
			ArrayList<String> friends = new ArrayList<String>();
			boolean flag=false;
			try {
				for(String room:rooms) {
					FutureGet futureGet = _dht.get(Number160.createHash(room)).start();
					futureGet.awaitUninterruptibly();
					if (futureGet.isSuccess()) {
						HashSet<Peer_nick_address> peers_on_topic;
						peers_on_topic = (HashSet<Peer_nick_address>) futureGet.dataMap().values().iterator().next().object();
						for(Peer_nick_address peer:peers_on_topic){
							flag=false;
							for(String friend:friends) {
								if(friend.equals(peer.getNick())) {
									flag=true;
								}
							}
							if(!flag) {
								friends.add(peer.getNick());
							}
						}
						
					}
				}
				return friends;
			}catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public boolean leaveNetwork() {	
		try {
			for(String room:rooms) {
				FutureGet futureGet = _dht.get(Number160.createHash(room)).start();
				futureGet.awaitUninterruptibly();
				if (futureGet.isSuccess()) {
					if(futureGet.isEmpty()) 
						return false;
					HashSet<Peer_nick_address> peers_on_topic;
					peers_on_topic = (HashSet<Peer_nick_address>) futureGet.dataMap().values().iterator().next().object();
					System.out.println("prova "+peers_on_topic.toString());
					System.out.println(room+" ciaone "+peers_on_topic.remove(this.pna)+" prova "+this.pna.toString());
					_dht.put(Number160.createHash(room)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				}
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
