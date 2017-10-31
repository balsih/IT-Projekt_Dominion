package Server_MVC;

import Abstract_MVC.Model;
import Server_GameLogic.ServerThreadForClient;
import Server_Services.ServiceLocator;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:09:18
 */
public class Server_Model extends Model {

	private int port;


	public Server_Model(){
		super();
	}

	/**
	 * 
	 * @param port
	 */
	public void startServerSocket(int port){

	}
}//end Server_Model