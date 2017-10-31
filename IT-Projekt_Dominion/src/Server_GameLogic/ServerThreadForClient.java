import Klassendiagramm.Messages.Message;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:09:30
 */
public class ServerThreadForClient {

	private Socket clientSocket;
	private static HashMap connections;
	private Game gameThread;
	private Player player;
	private ServiceLocator sl;
	private Queue waitingMessages;
	public Game m_Game;
	public Player m_Player;



	public void finalize() throws Throwable {

	}
	private ServerThreadForClient(){

	}

	/**
	 * 
	 * @param clientSocket
	 */
	private addClientSocket(Socket clientSocket){

	}

	/**
	 * 
	 * @param inetAddress
	 * @param clientSocket
	 */
	public ServerThreadForClient getServerThreadForClient(InetAddress inetAddress, Socket clientSocket){
		return null;
	}

	/**
	 * 
	 * @param msgIn
	 */
	public Message processMessage(Message msgIn){
		return null;
	}

	public run(){

	}

	/**
	 * 
	 * @param message
	 */
	public sendMessage(Message message){

	}
}//end ServerThreadForClient