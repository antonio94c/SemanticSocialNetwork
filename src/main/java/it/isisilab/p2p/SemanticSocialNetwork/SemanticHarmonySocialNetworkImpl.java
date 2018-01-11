package it.isisilab.p2p.SemanticSocialNetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class SemanticHarmonySocialNetworkImpl implements SemanticHarmonySocialNetwork{
	final private Peer peer;
	final private PeerDHT _dht;
	final private int DEFAULT_MASTER_PORT=4000;
	final private String KEY_QUESTIONS_ROOM="question";
	
	private String profileKey=null;
	private Peer_nick_address pna=null;
	private ArrayList<String> rooms=null;

	public SemanticHarmonySocialNetworkImpl( int _id, String _master_peer, final MessageListener _listener) throws IOException{
		rooms=new ArrayList<String>();
		
		 peer= new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT+_id).start();
		_dht = new PeerBuilderDHT(peer).start();	
		
		FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
		fb.awaitUninterruptibly();
		if(fb.isSuccess()) {
			peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}
		peer.objectDataReply(new ObjectDataReply() {
			
			public Object reply(PeerAddress sender, Object request) throws Exception {
				return _listener.parseMessage(request);
			}
		});
	}
	
	public boolean putUserProfileQuestions(List<String> questions){
		try {
			FutureGet futureGet = _dht.get(Number160.createHash(KEY_QUESTIONS_ROOM)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty()) {
					_dht.put(Number160.createHash(KEY_QUESTIONS_ROOM)).data(new Data(new HashSet<String>())).start().awaitUninterruptibly();
				}
				futureGet = _dht.get(Number160.createHash(KEY_QUESTIONS_ROOM)).start();
				futureGet.awaitUninterruptibly();
				if (futureGet.isSuccess()) {
					HashSet<ArrayList<String>> peers_on_room;
					peers_on_room = (HashSet<ArrayList<String>>) futureGet.dataMap().values().iterator().next().object();
					peers_on_room.add((ArrayList<String>)questions);
					
					_dht.put(Number160.createHash(KEY_QUESTIONS_ROOM)).data(new Data(peers_on_room)).start().awaitUninterruptibly();
				}else {
					return false;
				}
			}else {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the social network users questions.
	 * @return a list of String that is the profile questions.
	 */
	public List<String> getUserProfileQuestions(){
		ArrayList<String> questions = new ArrayList<String>();
		try {
			FutureGet futureGet = _dht.get(Number160.createHash(KEY_QUESTIONS_ROOM)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess()) {
				HashSet<ArrayList<String>> peers_on_room;
				peers_on_room = (HashSet<ArrayList<String>>) futureGet.dataMap().values().iterator().next().object();
				for(ArrayList<String> question:peers_on_room){
					questions=question;
				}
			}
			return questions;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		ArrayList<String> nick_sended=new ArrayList<String>();
		boolean flag=false;
		try {
			rooms.add(_profile_key);
			
			String new_key="";
			for(int i=0;i<_profile_key.length();i++) {
				new_key=_profile_key;
				if(_profile_key.charAt(i)=='0')
					new_key=new_key.substring(0,i)+"1"+new_key.substring(i+1,new_key.length());
				else
					new_key=new_key.substring(0,i)+"0"+new_key.substring(i+1,new_key.length());
				rooms.add(new_key);
			}
			
			this.pna=new Peer_nick_address(_nick_name,_dht.peer().peerAddress());
			nick_sended.add(pna.getNick());
			
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
						HashSet<Peer_nick_address> peers_on_room;
						peers_on_room = (HashSet<Peer_nick_address>) futureGet.dataMap().values().iterator().next().object();
						peers_on_room.add(this.pna);
						_dht.put(Number160.createHash(room)).data(new Data(peers_on_room)).start().awaitUninterruptibly();
						for(Peer_nick_address nick_in_room:peers_on_room) {
							flag=false;
							for(String nick:nick_sended) {
								if(nick.equals(nick_in_room.getNick())) {
									flag=true;
									break;
								}
							}
							if(!flag) {
								FutureDirect futureDirect = _dht.peer().sendDirect(nick_in_room.getAddress()).object("ciao "+nick_in_room.getNick()+" sono "+this.pna.getNick()).start();
								futureDirect.awaitUninterruptibly();
								nick_sended.add(nick_in_room.getNick());
							}
						}
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
						HashSet<Peer_nick_address> peers_on_room;
						peers_on_room = (HashSet<Peer_nick_address>) futureGet.dataMap().values().iterator().next().object();
						for(Peer_nick_address peer:peers_on_room){
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
	
}
