package it.isisilab.p2p.SemanticSocialNetwork;

import java.io.Serializable;

import net.tomp2p.peers.PeerAddress;

public class Peer_nick_address implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PeerAddress address;
	public String nick;
		
	public Peer_nick_address(String nick, PeerAddress address) {
		super();
		this.address = address;
		this.nick = nick;
	}
	public PeerAddress getAddress() {
		return address;
	}
	public void setAddress(PeerAddress address) {
		this.address = address;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

}
