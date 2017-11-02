package Server_MVC;

import java.io.IOException;
import java.net.ServerSocket;

import java.util.logging.Logger;

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
	private final Logger logger = Logger.getLogger("");
	

	public Server_Model(){
		super();
	}

	//starts the server with the entered port
	public void startServerSocket(int port) throws IOException{
		String info = "Start Server";
		logger.info(info);
		try{
			this.listener = new ServerSocket(port);
		}catch (IOException e) {
			this.logger.info(e.toString());
		}
	}
	
	/*checks if the port value is valid
	 *Adapted from: 
	 * Prof. Bradley Richards, Package: ch.fhnw.richards.lecture02.email_validator, Class: EmailValidator_Model
	 * */
	protected boolean isValidPortNumber(String newValue) {
		boolean valid = true;

		try {
			int value = Integer.parseInt(newValue);
			if (value < 1 || value > 65535)
				valid = false;
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}
}//end Server_Model