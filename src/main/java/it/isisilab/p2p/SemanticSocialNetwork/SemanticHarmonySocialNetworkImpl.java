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
	
	private Peer_nick_address pna=null;
	private ArrayList<String> rooms=null;
	

	public SemanticHarmonySocialNetworkImpl( int _id, String _master_peer, final MessageListener _listener) throws IOException{
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
	
	
	/**
	 * Inserisce nella rete la lista delle domande da fare ai nuovi utenti per stabilire le amicizie, viene richiamato solo dal primo peer che crea la rete
	 * @param questions una lista di domande
	 * @return true se è andato a buon fine, false altrimenti
	 */
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
	 * Restituisce la lista delle domande a cui l'utente deve rispondere per creare le amicizie.
	 * @return la lista delle domande.
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
	 * Crea la chiave in base alle risposte date dall'utente.
	 * @param _answer la lista delle risposte.
	 * @return la chiave del nodo generata in base alle risposte date.
	 */
	public String createAuserProfileKey(List<Integer> _answer) {
		String profileKey="";
		for(int i=0;i<_answer.size();i++) {
			profileKey=profileKey+_answer.get(i);
		}
		return profileKey;
	}
	
	
	/**
	 * Genera le stanze degli amici in base alla chiave del nodo, diventano amici i nodi che hanno risposto alle domande allo stesso modo o con al più una domanda di differenza
	 * @param profile_key la chiave del nodo generata in base alle risposte date.
	 */
	private void generate_rooms(String profile_key){
		rooms=new ArrayList<String>();
		rooms.add(profile_key);
		
		String new_key="";
		for(int i=0;i<profile_key.length();i++) {
			new_key=profile_key;
			if(profile_key.charAt(i)=='0')
				new_key=new_key.substring(0,i)+"1"+new_key.substring(i+1,new_key.length());
			else
				new_key=new_key.substring(0,i)+"0"+new_key.substring(i+1,new_key.length());
			rooms.add(new_key);
		}
	}
	
	/**
	 * Il nodo si unisce alla rete e viene generato un messaggio automatico per ogni potenziale amico.
	 * @param _profile_key la chiave del nodo generata in base alle risposte date.
	 * @param _nick_name in nickname dell'utente.
	 * @return true se il join va a buon fine, false altrimenti.
	 */
	public boolean join(String _profile_key,String _nick_name) {
		ArrayList<String> nick_sended=new ArrayList<String>();
		boolean flag=false;
		
		generate_rooms(_profile_key);
		
		this.pna=new Peer_nick_address(_nick_name,_dht.peer().peerAddress());
		nick_sended.add(pna.getNick());
		
		try {
			for(String room:rooms) {
				FutureGet futureGet = _dht.get(Number160.createHash(room)).start();
				futureGet.awaitUninterruptibly();
				if (futureGet.isSuccess()) {
					if(futureGet.isEmpty()) {
						_dht.put(Number160.createHash(room)).data(new Data(new HashSet<Peer_nick_address>())).start().awaitUninterruptibly();
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
								FutureDirect futureDirect = _dht.peer().sendDirect(nick_in_room.getAddress()).object("ciao "+nick_in_room.getNick()+", "+this.pna.getNick()+" è un nuovo utente della rete e potrebbe essere un tuo potenziale amico").start();
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
	 * Restituisce dei nickname degli amici del nodo. 
	 * @return la lista degli amici.
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
							if(peer.getNick().equals(this.pna.getNick())) {
								continue;
							}
							flag=false;
							for(String friend:friends) {
								if(friend.equals(peer.getNick())) {
									flag=true;
									break;
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
	
	private boolean leaveRooms(String room) {
		try {
			FutureGet futureGet = _dht.get(Number160.createHash(room)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty()) return false;
				HashSet<Peer_nick_address> peers_on_room;
				peers_on_room = (HashSet<Peer_nick_address>) futureGet.dataMap().values().iterator().next().object();
				for(Peer_nick_address p:peers_on_room) {
					if(p.getAddress().equals(this.pna.getAddress()) && p.getNick().equals(this.pna.getNick())) {
						peers_on_room.remove(p);
						break;
					}
				}
				_dht.put(Number160.createHash(room)).data(new Data(peers_on_room)).start().awaitUninterruptibly();
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean leaveNetwork() {	
		for(String room:rooms) leaveRooms(room);
		_dht.peer().announceShutdown().start().awaitUninterruptibly();
		return true;
	}
	
	
	
}
