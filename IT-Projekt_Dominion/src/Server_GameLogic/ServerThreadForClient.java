package Server_GameLogic;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;

import Messages.Message;
import Server_Services.ServiceLocator;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:09:30
 */
public class ServerThreadForClient implements Runnable {

	private Socket clientSocket;
	private static HashMap<InetAddress, ServerThreadForClient> connections;
	private Game gameThread;
	private Player player;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private Queue<Message> waitingMessages;


	private ServerThreadForClient(){

	}

	/**
	 * 
	 * @param clientSocket
	 */
	private void addClientSocket(Socket clientSocket){

	}

	/**
	 * 
	 * @param inetAddress
	 * @param clientSocket
	 */
	public static ServerThreadForClient getServerThreadForClient(InetAddress inetAddress, Socket clientSocket){
		return null;
	}

	/**
	 * 
	 * @param msgIn
	 */
	public Message processMessage(Message msgIn){
		return null;
	}

	@Override
	public void run(){

	}

	/**
	 * 
	 * @param message
	 */
	public void sendMessage(Message message){

	}
}//end ServerThreadForClient