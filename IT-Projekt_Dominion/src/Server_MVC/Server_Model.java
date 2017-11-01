package Server_MVC;

import java.io.IOException;
import java.net.ServerSocket;

import com.sun.istack.internal.logging.Logger;

import Abstract_MVC.Model;
import Server_GameLogic.ServerThreadForClient;
import Server_Services.ServiceLocator;

/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:09:18
 */
public class Server_Model extends Model {

	private int port;
	private ServerSocket listener;
//	private final Logger logger = Logger.getLogger("", null);
	

	public Server_Model(){
		super();
	}

	/**
	 * 
	 * @param port
	 * @throws IOException 
	 */
	public void startServerSocket(int port) throws IOException{
		String info = "Start Server";
		this.listener = new ServerSocket(port);
//		logger.info(info);
	}
}//end Server_Model