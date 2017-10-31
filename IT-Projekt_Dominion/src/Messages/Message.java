package Klassendiagramm.Messages;


/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:18
 */
public abstract class Message {

	private static final String ATTR_CLIENT;
	private static final String ATTR_ID;
	private static final String ATTR_TIMESTAMP;
	private static final String ATTR_TYPE;
	private String client;
	private static final String ELEMENT_MESSAGE;
	private long id;
	private static long messageID;
	private long timestamp;
	private Document xmlDocument;
	private String xmlString;
	public GameApp_Model m_GameApp_Model;
	public Game m_Game;



	public void finalize() throws Throwable {

	}
	protected Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	protected abstract addNodes(Document docIn);

	private buildMessage(){

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
	protected abstract init(Document docIn);

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
	public send(Socket s){

	}

	/**
	 * 
	 * @param client
	 */
	public setClient(String client){

	}

	/**
	 * 
	 * @param id
	 */
	public setId(long id){

	}

	/**
	 * 
	 * @param timestamp
	 */
	public setTimestamp(long timestamp){

	}

	public String toString(){
		return "";
	}
}//end Message