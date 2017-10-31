package Messages;

import java.net.Socket;

import com.sun.xml.internal.txw2.Document;

import Client_GameApp_MVC.GameApp_Model;
import Server_GameLogic.Game;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:18
 */
public abstract class Message {

	private static final String ATTR_CLIENT = "client";
	private static final String ATTR_ID = "id";
	private static final String ATTR_TIMESTAMP = "timestamp";
	private static final String ATTR_TYPE = "type";
	private String client;
	private static final String ELEMENT_MESSAGE = "message";
	private long id;
	private static long messageID;
	private long timestamp;
	private Document xmlDocument;
	private String xmlString;
	public GameApp_Model m_GameApp_Model;
	public Game m_Game;


	protected Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	protected abstract void addNodes(Document docIn);

	private void buildMessage(){

	}

	public String getClient(){
		return "";
	}

	public long getId(){
		return 0;
	}

	public long getTimestamp(){
		return 0;
	}

	public String getXmlString(){
		return "";
	}

	/**
	 * 
	 * @param docIn
	 */
	protected abstract void init(Document docIn);

	private static long nextMessageID(){
		return 0;
	}

	/**
	 * 
	 * @param socket
	 */
	public static Message receive(Socket socket){
		return null;
	}

	/**
	 * 
	 * @param s
	 */
	public void send(Socket s){

	}

	/**
	 * 
	 * @param client
	 */
	public void setClient(String client){

	}

	/**
	 * 
	 * @param id
	 */
	public void setId(long id){

	}

	/**
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp){

	}

	public String toString(){
		return "";
	}
}//end Message